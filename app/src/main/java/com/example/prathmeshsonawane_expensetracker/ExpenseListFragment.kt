package com.example.prathmeshsonawane_expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prathmeshsonawane_expensetracker.databinding.FragmentMainPageBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.exp

class ExpensesListFragment : Fragment() {
    //Declare Binding, set later
    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!

    //Create ViewModel
    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((activity?.application as ExpenseApp).repository)
    }
    //Create Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    // main screen list, set up recycler, option to filter by date/category
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set up all elements on main View screen
        createCategory()
        createDate()
        setupRecyclerView()

        binding.addExpenseButton.setOnClickListener {
            //Navigate to page that lists Expenses
            val action = ExpensesListFragmentDirections.actionExpensesListFragmentToAddOrEditExpenseFragment(0) // 0 for new expense
            findNavController().navigate(action)
        }

        //Navigate to Page that will later list categories.
//        binding.CategorizedButton.setOnClickListener {
//            findNavController().navigate(R.id.action_expensesListFragment_to_expenseCategoryFragment)
//        }
    }

    // call the recycler for all expense entries

    private fun createCategory() {
        val categories = resources.getStringArray(R.array.main_page_spinner_categories)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategoryFilter.adapter = spinnerAdapter

        //Create Listener
        binding.spinnerCategoryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val Category = categories[position]
                //If Current category is All, we should display everything.
                if (Category == "All") {
                    viewModel.allExpenses.observe(viewLifecycleOwner) { allexpenses ->
                        (binding.expensesRecyclerView.adapter as ExpensesAdapter).submitList(allexpenses)
                    }
                    //Else, get the expenses by Category
                } else {
                    viewModel.getExpensesByCategory(Category).observe(viewLifecycleOwner) { expenseCat ->
                        (binding.expensesRecyclerView.adapter as ExpensesAdapter).submitList(expenseCat)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setupRecyclerView() {
        //Pass in onClick function to open a certain Expense
        val adapter = ExpensesAdapter { expense ->
            val action = ExpensesListFragmentDirections.actionExpensesListFragmentToAddOrEditExpenseFragment(expense.id)
            findNavController().navigate(action)
        }
        //set Bindings with the adapter
        binding.expensesRecyclerView.adapter = adapter
        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            adapter.submitList(expenses)
        }
    }
    // filter expenses based on date
    private fun createDate() {
        binding.FilterButton.setOnClickListener {
            val stringOfDate = binding.dateSelecter.text.toString()
            if (stringOfDate.isNotEmpty()) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                try {
                    val parsedDate: Date = dateFormat.parse(stringOfDate)
                    val unixTimestamp = parsedDate.time
                    val formattedTimestamp = unixTimestamp.toString()
                    viewModel.filterExpensesByDate(formattedTimestamp).observe(viewLifecycleOwner) { filteredExpenses ->
                        (binding.expensesRecyclerView.adapter as ExpensesAdapter).submitList(filteredExpenses)
                    }
                } catch (e: Exception) {
                    //If Exception, just catch it and implement further if needed
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
