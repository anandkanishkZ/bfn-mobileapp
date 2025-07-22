package com.natrajtechnology.bfn.utils

object CloudinaryConfig {
    
    // Your actual Cloudinary credentials from the dashboard
    const val CLOUD_NAME = "dnsheb8nm"  // Your cloud name from screenshot ✅
    const val API_KEY = "138277111495987"  // Your API key from screenshot ✅
    const val API_SECRET = "QJ4w3GN3e2c4ZcvCTuIeTqeHc-Q"  // Your API secret from screenshot ✅
    
    // Upload preset name (create this in your Cloudinary settings)
    const val UPLOAD_PRESET = "blood_for_nepal_preset"
    
    // Folder structure
    const val PROFILE_PICTURES_FOLDER = "blood_for_nepal/profiles"
    
    // Image transformations
    const val PROFILE_IMAGE_TRANSFORMATION = "c_fill,w_400,h_400,q_auto,f_auto"
}

/**
 * Setup Instructions for Cloudinary:
 * 
 * 1. Go to your Cloudinary Dashboard (https://cloudinary.com/console)
 * 2. Copy your Cloud Name, API Key, and API Secret
 * 3. Replace the values in this file
 * 4. Create an Upload Preset:
 *    - Go to Settings -> Upload Presets
 *    - Click "Add upload preset"
 *    - Set preset name: "blood_for_nepal_preset"
 *    - Set mode: "Unsigned"
 *    - Set folder: "blood_for_nepal/profiles"
 *    - Add transformation: "c_fill,w_400,h_400,q_auto,f_auto"
 *    - Save the preset
 * 
 * 5. Your free tier includes:
 *    - 25 GB storage
 *    - 25 GB bandwidth per month
 *    - 1000 transformations per month
 *    - Perfect for a blood donation app!
 */
