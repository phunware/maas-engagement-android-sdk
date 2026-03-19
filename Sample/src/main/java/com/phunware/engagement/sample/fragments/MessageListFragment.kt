package com.phunware.engagement.sample.fragments;

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phunware.engagement.Engagement
import com.phunware.engagement.messages.model.Message
import com.phunware.engagement.sample.R
import com.phunware.engagement.sample.adapters.MessageAdapter
import com.phunware.engagement.sample.adapters.MessageAdapter.OnMessageSelectedListener
import com.phunware.engagement.sample.fragments.MessageDetailFragment.Companion.newInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

public class MessageListFragment : Fragment(), OnMessageSelectedListener {
    private lateinit var messageListRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorView: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val v = inflater.inflate(R.layout.fragment_message_list, container, false)
        messageListRecyclerView = v.findViewById<View>(R.id.list) as RecyclerView
        progressBar = v.findViewById<View>(R.id.loading) as ProgressBar
        errorView = v.findViewById<View>(R.id.error) as TextView
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppCompatActivity) {
            context.setTitle(R.string.nav_messages)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(messageListRecyclerView.context)
        messageListRecyclerView.layoutManager = layoutManager
        messageListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                messageListRecyclerView.context,
                layoutManager.orientation
            )
        )
        progressBar.visibility = View.VISIBLE
        errorView.visibility = View.GONE
        messageListRecyclerView.visibility = View.GONE
        CoroutineScope(Dispatchers.IO).launch {
            Engagement.fetchMessages(Date(0L), Date())
                .onSuccess { result ->
                    withContext(Dispatchers.Main) {
                        onMessages(result)
                    }
                }
                .onFailure { result ->
                    Log.e(
                        "", "Failed to get messages",
                        result
                    )
                }
        }
    }

    private fun onMessages(messages: List<Message>?) {
        if (messages.isNullOrEmpty()) {
            showEmpty()
            return
        }
        val adapter = MessageAdapter(messages, this)
        messageListRecyclerView.adapter = adapter
        progressBar.visibility = View.GONE
        errorView.visibility = View.GONE
        messageListRecyclerView.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        progressBar.visibility = View.GONE
        messageListRecyclerView.visibility = View.GONE
        errorView.visibility = View.VISIBLE
        errorView.setText(R.string.no_messages)
    }

    override fun onMessageSelected(message: Message) {
        val fragment = newInstance(message)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }
}