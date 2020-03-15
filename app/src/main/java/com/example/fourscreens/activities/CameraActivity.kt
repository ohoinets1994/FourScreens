package com.example.fourscreens.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.fourscreens.R
import org.opencv.android.*
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream

class CameraActivity : AppCompatActivity(), CvCameraViewListener {

    private lateinit var openCvCameraView: CameraBridgeViewBase
    private lateinit var grayScaleImage: Mat
    private var cascadeClassifier: CascadeClassifier? = null
    private var absoluteFaceSize = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actitvity_test)

        openCvCameraView = findViewById<View>(R.id.jcvOpenCV) as JavaCameraView
        openCvCameraView.setCvCameraViewListener(this)
    }

    private val loaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                initializeOpenCVDependencies()
            } else {
                super.onManagerConnected(status)
            }
        }
    }

    private fun initializeOpenCVDependencies() {
        try {
            val inputStream = resources.openRawResource(R.raw.haarcascade_frontalface_alt2)
            val cascadeDir = getDir("cascade", Context.MODE_PRIVATE)
            val mCascadeFile = File(cascadeDir, "lbpcascade_frontalface.xml")
            val outputStream = FileOutputStream(mCascadeFile)
            val buffer = ByteArray(4096)
            var bytesRead: Int

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            inputStream.close()
            outputStream.close()
            cascadeClassifier = CascadeClassifier(mCascadeFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        openCvCameraView.enableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        grayScaleImage = Mat(height, width, CvType.CV_8UC4)
        absoluteFaceSize = (height * 0.2)
    }

    override fun onCameraViewStopped() {}

    override fun onCameraFrame(inputFrame: Mat): Mat {

        Imgproc.cvtColor(inputFrame, grayScaleImage, Imgproc.COLOR_RGBA2RGB)

        val faces = MatOfRect()
        cascadeClassifier?.detectMultiScale(
            grayScaleImage, faces, 1.1, 2, 2,
            Size(absoluteFaceSize, absoluteFaceSize), Size()
        )

        val facesArray = faces.toArray()
        for (rect in facesArray)
            Imgproc.rectangle(
                inputFrame, rect.tl(), rect.br(),
                Scalar(0.0, 255.0, 0.0, 255.0), 3)

        Imgproc.circle(inputFrame, Point(10.0, 50.0), 50, Scalar(255.0, 0.0, 0.0))

        return inputFrame
    }

    override fun onResume() {
        super.onResume()
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, loaderCallback)
    }
}