package ca.unb.mobiledev.plantpal

import android.os.Bundle

class CameraActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        class CameraActivity : AppCompatActivity() {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_btn)


                // Register the activity listener
                setCameraActivityResultLauncher()

                val camButton = findViewById<Button>(R.id.camButton)

                camButton.setOnClickListener {
                    dispatchTakePictureIntent()
                }
        }

    }



    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    cameraActivityResultLauncher!!.launch(takePictureIntent)
                }
            }
        }
    private fun setCameraActivityResultLauncher() {
        // Handle the image capture result
        // TODO - Implement later
        cameraActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                galleryAddPic()
            }
        }

    }

}

