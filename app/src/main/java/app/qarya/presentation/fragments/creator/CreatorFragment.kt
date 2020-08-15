package app.qarya.presentation.fragments.creator

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.qarya.R
import app.qarya.model.ModelType
import app.qarya.model.models.Category
import app.qarya.model.models.Post
import app.qarya.model.models.SFile
import app.qarya.model.models.requests.PostSetter
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.base.MyDialogFragment
import app.qarya.presentation.vms.VMPost
import app.qarya.utils.PermissionsHelper
import app.qarya.utils.PicassoImageLoader
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_creator.*
import tn.core.util.Const
import java.io.File
import java.util.*

/**
 *
 * A simple [Fragment] subclass.
 */
class CreatorFragment : MyDialogFragment<VMPost>(), View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    var filesIDS: MutableList<Int> = ArrayList()
    var categories: List<Category>? = null


    // --------------------- For Location ---------------------
    internal var latitude = ""
    internal var longitude = ""
    private var location: Location? = null
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    // lists for permissions
    private var permissionsToRequest: ArrayList<String>? = null
    private val permissionsRejected = ArrayList<String>()
    private val permissions = ArrayList<String>()
    // --------------------------------------------------------

    var currentType = -1
    fun getType() : Int {
        if (currentType==-1)
            currentType = args.getInt(Const.TYPE, 0)
        return currentType
    }
    val isNote: Boolean
        get() {
            return getType() === ModelType.NOTE
        }
    val isProduct: Boolean
        get() {
            return getType() === ModelType.PRODUCT
        }
    val isStory: Boolean
        get() {
            return getType() === ModelType.STORY
        }
    val isPost: Boolean
        get() {
            return getType() === ModelType.POST
        }


    companion object {
        fun newInstance(type: Int): CreatorFragment {
            val args = Bundle()
            args.putInt(Const.TYPE, type)
            val fragment = CreatorFragment()
            fragment.arguments = args
            return fragment
        }

        private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        private val UPDATE_INTERVAL: Long = 5000
        private val FASTEST_INTERVAL: Long = 5000 // = 5 seconds
        // integer for permissions results request
        private val MAPS_PERMISSIONS_RESULT = 1011
        private val IMAGE_PERMISSIONS_RESULT = 1012

        // pics selection
        const val RESULT_GALLERY = 11
        const val MAX_PHOTOS = 4
    }
    // pics selection
    var countPhotos = 0
    private var listImages: ArrayList<Img> = ArrayList<Img>()
    var uploaded = 0





    // -----------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------- LIFE CYCLE ---------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(VMPost::class.java)
        mViewModel.callErrors.observe(this, Observer { errors: List<String?>? -> onError(errors) })
        mViewModel.loadStatus.observe(this, Observer { b: Boolean? -> onStatusChanged(b) })
        mViewModel.getLiveData().observe(this, Observer { post: Post -> this.onDataReceived(post) })
        mViewModel.categories.observe(this, Observer { data: List<Category>? -> fillSpinner(data) })
        mViewModel.upload.observe(this, Observer { data: SFile -> this.onDataReceived(data) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_creator, container, false)
        //val v = inflater.inflate(R.layout.fragment_creator, container, false)
        //initDefaultViews(v)
        //return v
    }

    fun setViews(){
        if(isProduct){
            categoryLayout.visibility = View.VISIBLE
            titleLayout.visibility = View.VISIBLE
            addressLayout.visibility = View.VISIBLE
            productDetailsLayout.visibility = View.VISIBLE

            titleLayout.hint = getText(R.string.product_name)

            //shippingSpinner!!.adapter = ArrayAdapter(context!!, R.layout.spinner_dropdown_item, R.string.chat)

            mViewModel.categoriesOfProducts()
            checkAndAlertGPS()
        } else if (isStory) {
            //imagePicker.setLayoutParams(LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        } else if (isNote) {
            categoryLayout.visibility = View.VISIBLE
            titleLayout.visibility = View.VISIBLE
            descriptionLayout.visibility = View.VISIBLE

            titleLayout.hint = getText(R.string.note_title)
            descriptionLayout.hint  = getText(R.string.note_text)
            mViewModel.categoriesOfNotes()
        } else {
            descriptionLayout.visibility = View.VISIBLE
            descriptionLayout.hint  = getText(R.string.post_hint)
        }
        //imagePicker.setOnClickListener(this)
        picture.setOnClickListener(this)
        sendBtn.setOnClickListener{ view1: View? -> publish() }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews()

        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionsToRequest = permissionsToRequest(permissions)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest!!.size > 0) {
                requestPermissions(permissionsToRequest!!.toTypedArray(), MAPS_PERMISSIONS_RESULT)
            }
        }
        // we build google api client
        googleApiClient = GoogleApiClient.Builder(activity!!).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build()
    }


    override fun onStart() {
        super.onStart()

        if (googleApiClient != null) {
            googleApiClient!!.connect()
            //Toast.makeText(getActivity(), "GoogleApiClient Connected", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onResume() {
        super.onResume()

        if (!checkPlayServices()) {
            Toast.makeText(activity, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()

        // stop location updates
        if (googleApiClient != null && googleApiClient!!.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
            googleApiClient!!.disconnect()
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------- PUBLISHING ---------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    fun publish() {
        if (ready()) {
            if (listImages.size > 0) // upload file first
                uploadNext()
            else
                save()
        }
    }


    fun uploadNext() {// R.string.uploading_pictures
        var i = 0
        for (img in listImages) {
            if (!img.uploaded) {
                MyActivity.log("upload the "+(i+1)+"th image")
                mViewModel!!.upload(i, File(img.path))
                return
            }
            i++
        }
        //if(i >= (listImages.size-1))
            save()
    }

    fun save(){
        val t = titleEditText!!.text.toString()
        val d = descriptionEditText!!.text.toString()
        val a = addressEditText.text?.toString() ?: ""
        val phone = phoneTV.text?.toString()  ?: ""
        val price = priceTV.text?.toString()  ?: ""

        var c = 1
        if (categorySpinner!!.selectedItemPosition >= 0 && categorySpinner!!.selectedItemPosition < categories!!.size)
            c = categories!![categorySpinner!!.selectedItemPosition].id
        val shipping = shippingSpinner.selectedItemPosition
        if (filesIDS.size > 0)
            mViewModel!!.push(getType(), PostSetter(c, t, d, a, longitude, latitude, phone, price, shipping, filesIDS))
        else
            mViewModel!!.push(getType(), PostSetter(c, t, d, a, longitude, latitude, phone, price, shipping))
    }


    fun ready() : Boolean {
        if (isProduct && !checkAndAlertGPS()) {
            Toast.makeText(context, "Location required for your store", Toast.LENGTH_SHORT).show()
            return false
        }
        if ((isNote || isProduct) && titleEditText!!.text.toString().isEmpty()) {
            Toast.makeText(context, ( if(isNote) "title required" else "name required" ), Toast.LENGTH_SHORT).show()
            return false
        }
        if ((isPost || isNote) && descriptionEditText!!.text.toString().isEmpty()) {
            Toast.makeText(context, "description required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (isProduct && addressEditText!!.text.toString().isEmpty()) {
            Toast.makeText(context, "address required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (isStory && listImages.isEmpty()) {
            Toast.makeText(context, "image required", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    // -----------------------------------------------------------------------------------------------------------------
    // --------------------------------------------------- RESPONSES ---------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    fun onDataReceived(data: SFile) {
        Toast.makeText(context, "File SAVED :D", Toast.LENGTH_SHORT).show()
        filesIDS.add(data.id)
        listImages.forEachIndexed { index, img ->
            if(index==data.positionInLocalList)
                img.uploaded = true
        }
        //val v = listImages.get(data.positionInLocalList);
        //v.uploaded = true
        //listImages.set(data.positionInLocalList, v)
        uploadNext()
    }

    fun onDataReceived(post: Post) {
        MyActivity.log("Post: " + post.title + " created !")
        dismiss()
        (activity as MyActivity?)!!.onItemSelected(post)
        //((MyActivity) getActivity()).setFragment(TopicFragment.newInstance(post.getId()));
    }

    fun fillSpinner(data: List<Category>?) {
        categories = data
        val list: MutableList<String> = ArrayList()
        if (data != null) for (category in data) {
            list.add(category.name)
        }
        categorySpinner!!.adapter = ArrayAdapter(context!!, R.layout.spinner_dropdown_item, list)
    }

    // -----------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------- PICK A FILE --------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    override fun onClick(v: View) {
        if (countPhotos < MAX_PHOTOS) {
            showFileChooser()
        } else {
            Toast.makeText(context, resources.getString(R.string.message_max_photos), Toast.LENGTH_SHORT).show()
        }
    }


    //permission is automatically granted on sdk<23 upon installation
    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity!!.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                MyActivity.log("Permission is granted")
                return true
            } else {
                MyActivity.log("Permission is revoked")
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsHelper.showExplanation(activity,"Permission Needed", "Rationale", Manifest.permission.WRITE_EXTERNAL_STORAGE, IMAGE_PERMISSIONS_RESULT)
                } else {
                    ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), IMAGE_PERMISSIONS_RESULT)
                }
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            MyActivity.log("Permission is granted")
            return true
        }
    }






    private fun showFileChooser() {
        if (isStoragePermissionGranted()) {
            ImagePicker.create(this)
                    //.returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                    .folderMode(false) // folder mode (false by default)
                    .toolbarFolderTitle("Watan") // folder selection title
                    .toolbarImageTitle("Tap to select") // image selection title
                    .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                    .includeVideo(false) // Show video on image picker
                    //.single() // single mode
                    //.multi() // multi mode (default mode)
                    .limit(MAX_PHOTOS) // max images can be selected (99 by default)
                    .showCamera(true) // show camera or not (true by default)
                    .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                    //.origin(images) // original selected images, used in multi mode
                    //.exclude(images) // exclude anything that in image.getPath()
                    //.excludeFiles(files) // same as exclude but using ArrayList<File>
                    //.theme(R.style.CustomImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
                    .enableLog(false) // disabling log
                    .imageLoader(PicassoImageLoader()) // custom image loader, must be serializeable
                    .start() // start image picker activity with request code
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            //List<Image> images = ImagePicker.getImages(data);
            // or get a single image only

            /*val image = ImagePicker.getFirstImageOrNull(data)
            if (image != null){
                setImageSelector(image.path)
                //createNewPhoto(Uri.parse(image.path))
            }
            else
                Toast.makeText(context, "IMAGE NULL", Toast.LENGTH_SHORT).show()*/

            //createNewPhoto(data!!.getData()!!)

            val images:List<Image> = ImagePicker.getImages(data)
            for (image:Image in images){
                MyActivity.log(image.name, image.path)
                createNewPhoto(Uri.parse(image.path)) //createNewPhoto(data.getData())
            }
        }
    }










    // -----------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------- LOCALISATION --------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    fun hasPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.checkSelfPermission(activity!!, permission) == PackageManager.PERMISSION_GRANTED
        } else true

    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(activity)

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
            } else {
                Toast.makeText(activity, "ERRRoR !", Toast.LENGTH_SHORT).show()
                //finish();
            }

            return false
        }

        return true
    }

    fun checkAndAlertGPS(): Boolean {
        val lm = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder(activity)
                    .setMessage(R.string.msg_gps_network_not_enabled)
                    .setPositiveButton(R.string.open_location_settings) { paramDialogInterface, paramInt -> activity!!.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                    .setNegativeButton(R.string.Cancel, null)
                    .show()
        }
        return gps_enabled && network_enabled
    }



    private fun permissionsToRequest(wantedPermissions: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()

        for (perm in wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }

        return result
    }



    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)

        if (location != null) {
            MyActivity.log("Latitude : " + location!!.latitude + "\nLongitude : " + location!!.longitude)
            //Toast.makeText(getActivity(), "Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            latitude = location!!.latitude.toString()
            longitude = location!!.longitude.toString()
        }

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        locationRequest = LocationRequest()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = UPDATE_INTERVAL
        locationRequest!!.fastestInterval = FASTEST_INTERVAL

        if (ActivityCompat.checkSelfPermission(activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show()
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
    }

    override fun onConnectionSuspended(i: Int) {}

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            MyActivity.log("Latitude : " + location.latitude + "\nLongitude : " + location.longitude)
            //Toast.makeText(getActivity(), "Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            latitude = location.latitude.toString()
            longitude = location.longitude.toString()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MAPS_PERMISSIONS_RESULT -> {
                for (perm in permissionsToRequest!!) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm)
                    }
                }
                if (permissionsRejected.size > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected[0])) {
                            AlertDialog.Builder(activity).setMessage("These permissions are mandatory to get your location. You need to allow them.").setPositiveButton("OK") { dialogInterface, i ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(permissionsRejected.toTypedArray(), MAPS_PERMISSIONS_RESULT)
                                }
                            }.setNegativeButton("Cancel", null).create().show()

                            return
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient!!.connect()
                    }
                }
            }
            IMAGE_PERMISSIONS_RESULT -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MyActivity.log("Permission: " + permissions[0] + "was " + grantResults[0])
                    //resume tasks needing this permission
                    showFileChooser()
                }
            }
        }
    }











    ///////////////////////////////////////////////////////////////////////////////////
    internal class Img(uploaded: Boolean, uri: Uri) {
        //var compressed = false
        var uploaded = false
        var path: String? = ""
        var uri: Uri

        init {
            this.uploaded = uploaded
            this.uri = uri
            path = uri.path //getRealPathFromUri(getContext(), uri);
            MyActivity.log("the new PATH is: $path")
        }
    }


    private fun createNewPhoto(uri1: Uri) {
        try {
            val width = resources.getDimension(R.dimen.with_simple_drawee).toInt()
            val raduis = resources.getDimension(R.dimen.raduis_img).toInt()
            val marginLeft = resources.getDimension(R.dimen.raduis_img).toInt()
            val widthBtn = resources.getDimension(R.dimen.with_btn).toInt()
            countPhotos++
            val layoutParams = LinearLayout.LayoutParams(width, width)
            layoutParams.setMargins(0, 0, marginLeft, 0)
            val lpPhoto = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            val deleteBtnLayout = RelativeLayout.LayoutParams(widthBtn, widthBtn)
            deleteBtnLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            val deleteBtn = Button(context)
            deleteBtn.setBackgroundResource(R.drawable.close)
            deleteBtn.layoutParams = deleteBtnLayout
            val relativeLayout = RelativeLayout(context)
            relativeLayout.layoutParams = layoutParams
            val elImg = ImageView(context)
            elImg.layoutParams = lpPhoto
            relativeLayout.addView(elImg)
            relativeLayout.addView(deleteBtn)
            containerPhotos.addView(relativeLayout, 0)

            //RoundingParams roundingParams = RoundingParams.fromCornersRadius(raduis);
            //simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
            elImg.setImageURI(uri1)
            listImages.add(Img(false, uri1))
            deleteBtn.setOnClickListener { deletePhoto(relativeLayout, uri1) }
        } catch (e: java.lang.Exception) {
            MyActivity.log("compressing filed")
            e.printStackTrace()
        }
    }

    private fun deletePhoto(relativeLayout: RelativeLayout, uri: Uri) {
        val i = getPositionOfUri(uri)
        if (i < listImages.size) {
            listImages.removeAt(i)
            countPhotos--
            containerPhotos!!.removeView(relativeLayout)
        }
    }

    private fun getPositionOfUri(uri: Uri): Int {
        for (i in listImages.indices) {
            if (listImages[i].uri === uri) return i
        }
        return 0
    }

}