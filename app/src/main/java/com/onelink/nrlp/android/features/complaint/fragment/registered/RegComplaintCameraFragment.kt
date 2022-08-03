package com.onelink.nrlp.android.features.complaint.fragment.registered

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentRegComplaintCameraBinding
import com.onelink.nrlp.android.features.complaint.view.RegComplaintActivity
import com.onelink.nrlp.android.features.complaint.viewmodel.RegComplaintSharedViewModel
import com.onelink.nrlp.android.features.login.viewmodel.LoginFragmentViewModel
import com.onelink.nrlp.android.utils.ComplaintRequestModelConstants
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.removeDashes
import dagger.android.support.AndroidSupportInjection
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RegComplaintCameraFragment :
    BaseFragment<RegComplaintSharedViewModel, FragmentRegComplaintCameraBinding>(
        RegComplaintSharedViewModel::class.java
    ) {

    private val CAMERA_REQUEST_CODE = 100
    private val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    private var imageCapture: ImageCapture? = null
    private var imageTaken = false

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.fragment_reg_complaint_camera

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): RegComplaintSharedViewModel =
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(RegComplaintSharedViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        (activity as RegComplaintActivity).showToolbar()
        initListeners()
        initObservers()
        /*initOnFocusChangeListeners()
        initViews()*/
    }

    private fun initListeners() {
        if (checkIfPermissionGranted()) {
            startCamera()
        } else {
            requestPermissions(
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
            )
        }
        binding.btnSubmit.setOnClickListener {
            if (imageTaken) {
                val reqJson = viewModel.getBankComplaintObject(1, getUserType())
                viewModel.makeComplainCall(reqJson)
            } else
                Toast.makeText(context, getString(R.string.receipt_photo), Toast.LENGTH_SHORT)
                    .show()
        }
        binding.btnCapture.setOnClickListener {
            takePicture()
        }
        binding.btnEdit.setOnClickListener {
            imageTaken = false
            binding.ivCapture.visibility = View.GONE
        }
        //viewModel.transactionId.value
    }

    private fun initObservers() {
        viewModel.observeAddComplainResponse()
            .observe(this, androidx.lifecycle.Observer { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        oneLinkProgressDialog.hideProgressDialog()
                        response.data?.let {
                            viewModel.complaintId.postValue(it.complaintId)
                            sendNotification(
                                "Complaint Registered",
                                it.message, //"Complaint registered with ID ${it.complaintId}",
                                getString(R.string.channel_id)
                            )
                            viewModel.gotoComplaintResponseFragment(resources, fragmentHelper)
                        }
                    }
                    Status.LOADING -> {
                        oneLinkProgressDialog.showProgressDialog(activity)
                    }
                    Status.ERROR -> {
                        oneLinkProgressDialog.hideProgressDialog()
                        showGeneralErrorDialog(this, response.error)
                    }
                }
            })
    }

    private fun takePicture() {
        if (checkIfPermissionGranted()) {
            var outputStream: FileOutputStream? = null
            try {
                val file = RegComplaintCameraFragment.createFile(requireContext(), "jpg")
                outputStream = FileOutputStream(file)
                //textureView.bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                binding.ivCapture.visibility = View.VISIBLE
                val bitmap = binding.viewFinder.bitmap
                binding.ivCapture.setImageBitmap(bitmap)
                imageTaken = true

            } catch (e: java.lang.Exception) {
            }
        } else {
            showSettingsDialog()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = context?.let { ProcessCameraProvider.getInstance(it) }
        cameraProviderFuture?.addListener(Runnable {

            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e("CAM", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    private fun checkIfPermissionGranted(): Boolean {
        var granted = true
        for (permission in CAMERA_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            )
                granted = false
            break
        }
        return granted
    }

    private fun getUserType(): String {
        UserData.getUser()?.let {
            if (it.accountType == Constants.BENEFICIARY.toLowerCase(Locale.getDefault()))
                return Constants.BENEFICIARY
            else
                return Constants.REMITTER
        }
        return Constants.REMITTER
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            // If all permissions granted , then start Camera
            if (checkIfPermissionGranted()) {
                startCamera()
                Toast.makeText(context, "granted", Toast.LENGTH_LONG).show()
            } else {
                // If permissions are not granted,
                // present a toast to notify the user that
                // the permissions were not granted.
                Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                //finish()
            }
        }
    }

    private fun showSettingsDialog() {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("Need Permissions")
        builder.setMessage("Camera permission is required. You can grant it in app settings.")
        builder.setPositiveButton(
            "GOTO SETTINGS"
        ) { dialog, _ ->
            dialog.cancel()
            (activity as RegComplaintActivity).openSettings()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegComplaintCameraFragment()
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

        /** create file */
        @Suppress("SameParameterValue")
        fun createFile(context: Context, extension: String): File {
            val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.getDefault())
            val picturesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File(picturesDirectory, "IMG_${sdf.format(Date())}.$extension")
        }
    }
}