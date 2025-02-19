package ru.fomin.shoppinglistcleanarch.presentation.shopitem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import ru.fomin.shoppinglistcleanarch.R
import ru.fomin.shoppinglistcleanarch.databinding.ActivityShopItemBinding
import ru.fomin.shoppinglistcleanarch.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"

        private const val EXTRA_SCREEN_MODE = "screen_mode"

        private const val SCREEN_MODE_ADD = "screen_mode_add"
        private const val SCREEN_MODE_EDIT = "screen_mode_edit"
        private const val SCREEN_MODE_UNKNOWN = ""

        fun newIntentAddSHopItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, SCREEN_MODE_ADD)
            return intent
        }

        fun newIntentEditShopItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, SCREEN_MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }

    private val viewModel by viewModels<ShopItemViewModel>()
    private lateinit var binding: ActivityShopItemBinding

    private var screenMode = SCREEN_MODE_UNKNOWN

    // Depending on the screen mode, set in the addMode() and editMode() methods, and subsequently
    // called in the click listener at the button.
    private lateinit var buttonDoneAction: (String?, String?) -> Unit

    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        parseIntent()
        launchRightMode()
        setupListeners()
        observeViewModel()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("The EXTRA_SCREEN_MODE parameter is missing")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != SCREEN_MODE_ADD && mode != SCREEN_MODE_EDIT) {
            throw RuntimeException("Unknown EXTRA_SCREEN_MODE parameter. It must be \"SCREEN_MODE_ADD\" or \"SCREEN_MODE_EDIT\".")
        }
        screenMode = mode
        if (screenMode == SCREEN_MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("The EXTRA_SHOP_ITEM_ID parameter is missing")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun addMode() {
        buttonDoneAction = { name, count ->
            viewModel.addShopItem(name, count)
        }
    }

    private fun editMode() {
        buttonDoneAction = { name, count ->
            viewModel.editShopItem(name, count)
        }
        viewModel.getShopItem(shopItemId)
        viewModel.shopItemLiveData.observe(this) { shopItem ->
            setNameToEditText(shopItem.name)
            setCountToEditText(shopItem.count.toString())
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            SCREEN_MODE_ADD -> addMode()
            SCREEN_MODE_EDIT -> editMode()
        }
    }

    private fun getNameFromEditText(): String? {
        return binding.shopItemNameTextField.editText?.text?.toString()?.trim()
    }

    private fun setNameToEditText(name: String) {
        binding.shopItemNameTextField.editText?.setText(name)
    }

    private fun getCountFromEditText(): String? {
        return binding.shopItemCountTextField.editText?.text?.toString()?.trim()
    }

    private fun setCountToEditText(count: String) {
        binding.shopItemCountTextField.editText?.setText(count)
    }

    private fun setupListeners() {
        binding.shopItemNameTextField.editText?.doOnTextChanged { _, _, _, _ ->
            viewModel.clearNameFieldError()
        }

        binding.shopItemCountTextField.editText?.doOnTextChanged { _, _, _, _ ->
            viewModel.clearCountFieldError()
        }

        binding.doneButton.setOnClickListener {
            val name = getNameFromEditText()
            val count = getCountFromEditText()
            buttonDoneAction(name, count)
        }
    }

    private fun observeViewModel() {
        viewModel.nameFieldErrorLiveData.observe(this) { errorMessage ->
            binding.shopItemNameTextField.error = if (errorMessage) {
                getString(R.string.invalid_name)
            } else {
                null
            }
        }

        viewModel.countFieldErrorLiveData.observe(this) { errorMessage ->
            binding.shopItemCountTextField.error = if (errorMessage) {
                getString(R.string.invalid_count)
            } else {
                null
            }
        }

        viewModel.shouldCloseScreenLiveData.observe(this) {
            finish()
        }
    }
}