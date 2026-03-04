package com.phunware.engagement.sample.fragments;

import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phunware.engagement.Engagement
import com.phunware.engagement.Engagement.Companion.markMessageAsRead
import com.phunware.engagement.messages.model.Message
import com.phunware.engagement.sample.R
import com.phunware.engagement.sample.adapters.MessageDetailAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

public class MessageDetailFragment : Fragment(), MessageDetailAdapter.OnItemClickListener {

    public companion object {
        private val TAG = MessageDetailFragment::class.java.simpleName
        private const val EXTRA_MESSAGE = "message"
        private const val EXTRA_MESSAGE_ID = "message_id"
        public fun newInstance(messageId: String): MessageDetailFragment {
            val fragment = MessageDetailFragment()
            val args = Bundle().apply {
                putString(EXTRA_MESSAGE_ID, messageId)
            }
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        public fun newInstance(message: Message): MessageDetailFragment {
            val fragment = MessageDetailFragment()
            val args = Bundle().apply {
                putParcelable(EXTRA_MESSAGE, message)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var messageList: RecyclerView
    private lateinit var progress: ProgressBar
    private lateinit var messageAdapter: MessageDetailAdapter
    private var message: Message? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_message_detail,
            container, false
        )
        messageList = view.findViewById(R.id.list)
        progress = view.findViewById(R.id.progressBar)

        messageList.visibility = View.INVISIBLE
        progress.visibility = View.VISIBLE

        return view
    }

    override fun onResume() {
        if (message != null) {
            onMessage(message)
        }
        super.onResume()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(messageList.context)
        messageList.layoutManager = layoutManager
        messageList.addItemDecoration(
            DividerItemDecoration(
                messageList.context,
                layoutManager.orientation
            )
        )

        arguments?.getParcelable<Message>(EXTRA_MESSAGE)?.let { message ->
            onMessage(message)
        } ?: arguments?.getString(EXTRA_MESSAGE_ID)?.let { messageId ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    Engagement.fetchMessage(messageId = messageId)
                }.getOrNull()?.let(::onMessage)
            }
        } ?: onMessage(null)
    }

    private fun onMessage(message: Message?) {
        messageAdapter = MessageDetailAdapter(message, this)
        requireActivity().runOnUiThread {
            messageList.adapter = messageAdapter
            messageList.visibility = View.VISIBLE
            progress.visibility = View.INVISIBLE
        }
    }

    override fun onPromotionClicked(message: Message) {
        val fragment: Fragment = PromotionFragment.newInstance(message)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onMetadataClicked(message: Message) {
        val metadata = message.metadata
        val bold = StyleSpan(Typeface.BOLD)

        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.metadata)
            .setCancelable(true)
            .setItems(metadata?.map {
                SpannableStringBuilder(
                    getString(R.string.item_metadata, it.key, it.value)
                ).apply {
                    setSpan(bold, 0, it.key.length + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                }
            }?.toTypedArray()) { _: DialogInterface?, _: Int -> }
            .show()
    }

    override fun onReadClicked(message: Message) {
        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.mark_read_confirm_title)
            .setMessage(R.string.mark_read_confirm_message)
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _ -> dialog.dismiss() }
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _ ->
                markMessageRead(message)
                dialog.dismiss()
            }
            .show()
    }

    private fun markMessageRead(message: Message) {
        message.id?.let { id ->
            CoroutineScope(Dispatchers.IO).launch {
                markMessageAsRead(id)
                    .onSuccess {
                        val updatedMessage = message.copy(
                            id = id,
                            isRead = true,
                        )
                        this@MessageDetailFragment.message = updatedMessage
                        withContext(Dispatchers.Main) {
                            onMessage(updatedMessage)
                        }
                    }
                    .onFailure { result ->
                        Log.e(TAG, "Failed to set message read.", result)
                    }
            }
        }
    }
}
