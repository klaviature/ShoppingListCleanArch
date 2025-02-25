package ru.fomin.shoppinglistcleanarch.presentation.shopitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.fomin.shoppinglistcleanarch.R
import ru.fomin.shoppinglistcleanarch.databinding.FragmentShopItemBinding
import ru.fomin.shoppinglistcleanarch.domain.ShopItem

class ShopItemFragment : Fragment() {

    companion object {

        private const val SHOP_ITEM_ID = "shop_item_id"

        private const val INSTANCE_MODE = "instance_mode"

        private const val INSTANCE_MODE_ADD = "instance_mode_add"
        private const val INSTANCE_MODE_EDIT = "instance_mode_edit"
        private const val INSTANCE_MODE_UNKNOWN = ""

        fun newInstanceAddShopItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(INSTANCE_MODE, INSTANCE_MODE_ADD)
                }
            }
        }

        fun newInstanceEditShopItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(INSTANCE_MODE, INSTANCE_MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }

    private var TAG = "ShopItemFragment"

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var binding: FragmentShopItemBinding

    // Depending on the screen mode, set in the addMode() and editMode() methods, and subsequently
    // called in the click listener at the button.
    private lateinit var buttonDoneAction: (String?, String?) -> Unit

    private var instanceMode: String = INSTANCE_MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopItemBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        launchRightMode()
        setupListeners()
        observeViewModel()
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(INSTANCE_MODE)) {
            throw RuntimeException("The INSTANCE_MODE parameter is missing")
        }
        val mode = args.getString(INSTANCE_MODE)
        if (mode != INSTANCE_MODE_ADD && mode != INSTANCE_MODE_EDIT) {
            throw RuntimeException("Unknown INSTANCE_MODE parameter: $mode")
        }
        instanceMode = mode
        if (instanceMode == INSTANCE_MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("The SHOP_ITEM_ID parameter is missing")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID)
        }
    }

    private fun launchRightMode() {
        when (instanceMode) {
            INSTANCE_MODE_ADD -> addMode()
            INSTANCE_MODE_EDIT -> editMode()
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
        viewModel.shopItem.observe(viewLifecycleOwner) { shopItem ->
            setNameToEditText(shopItem.name)
            setCountToEditText(shopItem.count.toString())
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
        viewModel.nameFieldError.observe(viewLifecycleOwner) { errorMessage ->
            binding.shopItemNameTextField.error = if (errorMessage) {
                getString(R.string.invalid_name)
            } else {
                null
            }
        }

        viewModel.countFieldError.observe(viewLifecycleOwner) { errorMessage ->
            binding.shopItemCountTextField.error = if (errorMessage) {
                getString(R.string.invalid_count)
            } else {
                null
            }
        }

        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }
}