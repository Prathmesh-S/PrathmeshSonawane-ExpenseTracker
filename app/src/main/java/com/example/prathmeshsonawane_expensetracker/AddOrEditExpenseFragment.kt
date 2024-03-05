package com.example.prathmeshsonawane_expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.navArgs
import java.text.SimpleDateFormat
import java.util.*
import com.example.prathmeshsonawane_expensetracker.databinding.FragmentAddOrEditExpenseBinding

class  AddOrEditExpenseFragment : Fragment() {

    //Declare the binding
    private var _binding: FragmentAddOrEditExpenseBinding? = null
    private val binding get() = _binding!!

    //Create args so that we can utilize nav_graph and passing parameters.
    private val args:  AddOrEditExpenseFragmentArgs by navArgs()

    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((activity?.application as ExpenseApp).repository)
    }
    //Initialize Binding to FragmentAddOrEditExpense XML file
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddOrEditExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Create our Spinner
        createSpinner()

        //Load up proper expense information, including category, date, and amount of money
        val expenseId = args.ExpenceId
        if (expenseId > 0) {
            viewModel.getExpenseById(expenseId).observe(viewLifecycleOwner) { expense ->
                editPrevExpense(expense)
            }
        }
        //Navigate back to the homescreen AKA ExpenseListFragment
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.SaveButton.setOnClickListener {
            if (expenseId > 0) {
                updateExpense(expenseId)
            } else {
                //Since this is a new Expense.
                saveExpense()
            }
        }

    }

    // Load up a new spinner.
    private fun createSpinner() {
        val all_categories = resources.getStringArray(R.array.all_categories)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, all_categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }

    //This function is in charge of editing a previously made expense in conjunction with updateExpense.
    private fun editPrevExpense(expense: Expense) {
        val date = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        //Set Texts for both amount of money and the date
        binding.Date.setText(date.format(expense.date))
        binding.Amount.setText(expense.amount.toString())
        val categories = resources.getStringArray(R.array.all_categories)
        //Set spinner to the selected category
        binding.categorySpinner.setSelection(categories.indexOf(expense.category))
    }

   //Update an expense here.
    private fun updateExpense(id: Int) {
        //Create tri-uple?
        val (ExpenseAmount, ExpenseDate, ExpenseCategory) = getExpenseInput()
        if (ExpenseAmount != null &&  ExpenseDate != null) {
            val expense = Expense(id = id, date =  ExpenseDate, amount = ExpenseAmount, category = ExpenseCategory)
            viewModel.update(expense)
            findNavController().navigateUp()
        }
    }

    // Creating a new expense after clicking the save Button.
    private fun saveExpense() {
        val (ExpenseAmount, ExpenseDate, ExpenseCategory) = getExpenseInput()
        if (ExpenseAmount != null && ExpenseDate != null) {
            val expense = Expense(date = ExpenseDate, amount = ExpenseAmount, category = ExpenseCategory)
            viewModel.insert(expense)
            findNavController().navigateUp()
        }
    }

    // Getting Data from our Add Expense Form.
    private fun getExpenseInput(): Triple<Double?, Date?, String> {
        val amount = binding.Amount.text.toString().toDoubleOrNull()
        val stringOfDate = binding.Date.text.toString()
        val category = binding.categorySpinner.selectedItem.toString()

        if (amount == null) {
            return Triple(null, null, "")
        }

        val dateFormatted = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val date = try {
            dateFormatted.parse(stringOfDate)
        } catch (e: Exception) {
            //Should not get an exception. Can add fail implementation here if needed.
            null
        } ?: return Triple(null, null, "")

        return Triple(amount, date, category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}