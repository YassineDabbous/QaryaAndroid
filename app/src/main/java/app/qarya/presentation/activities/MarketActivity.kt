package app.qarya.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import app.qarya.R
import app.qarya.model.ModelType
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.fragments.CategoriesFragment
import app.qarya.presentation.fragments.HighlightsFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.MaterialSearchBar.OnSearchActionListener
import kotlinx.android.synthetic.main.activity_market.*

class MarketActivity : MyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)
        setBackground()
        setFirstFragment(HighlightsFragment.newInstance(ModelType.PRODUCT))

        bottomBar.onItemSelected = {
            //status.text = "Item $it selected"
            if (it == 0)
                setFragment(HighlightsFragment.newInstance(ModelType.PRODUCT))
            else if (it == 1)
                setFragment(HighlightsFragment.newInstance(ModelType.STORE))
            else if (it == 2)
                setFragment(HighlightsFragment.newInstance(ModelType.NOTE))
            else
                setFragment(HighlightsFragment.newInstance(ModelType.USER))
        }

        setBottomSheet()


        searchBar.setOnSearchActionListener(object:OnSearchActionListener{
            override fun onSearchStateChanged(enabled: Boolean) {
                log("onSearchStateChanged $enabled")
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                log("onSearchConfirmed $text")
            }

            override fun onButtonClicked(buttonCode: Int) {
                log("buttonCode $buttonCode")
            }

        });

    }


    fun setBottomSheet() {
        val sheetBehavior: BottomSheetBehavior<View>
        val bottom_sheet: CardView
        bottom_sheet = findViewById(R.id.bottom_sheet)
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        // click event for show-dismiss bottom sheet
        // click event for show-dismiss bottom sheet
        filterBtn.setOnClickListener(View.OnClickListener {
            if (sheetBehavior.getState() !== BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                //btn_bottom_sheet.setText("Close sheet")
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                //btn_bottom_sheet.setText("Expand sheet")
            }
        })
        // callback for do something
        // callback for do something
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        //btn_bottom_sheet.setText("Close Sheet")
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        //btn_bottom_sheet.setText("Expand Sheet")
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //
            }
        })
    }
}