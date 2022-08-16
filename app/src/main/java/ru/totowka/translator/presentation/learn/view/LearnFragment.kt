package ru.totowka.translator.presentation.learn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import ru.totowka.translator.R
import ru.totowka.translator.databinding.FragmentLearnBinding
import ru.totowka.translator.databinding.FragmentWordlistBinding
import ru.totowka.translator.utils.Common.string


/**
 * Фрагмент, отвечающий за экран изучения слов.
 */
class LearnFragment : Fragment() {
    private lateinit var binding: FragmentLearnBinding
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLearnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment)
//                as NavHostFragment
//        val navController = navHostFragment.navController
//        (activity as AppCompatActivity).setupActionBarWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                exit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun exit() {
        (activity as AppCompatActivity).supportFragmentManager
            .popBackStack()
    }

    companion object {
        const val TAG = "LearnFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [LearnFragment]
         */
        fun newInstance() = LearnFragment()
    }
}