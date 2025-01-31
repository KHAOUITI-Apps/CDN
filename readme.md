# **Build Your Own YouTube-like Video Streaming System with Signed URLs**

**KHAOUITI CDN** is a tutorial-based project that walks you through the process of building a YouTube-like video streaming system using Spring Boot, securing video access with signed URLs. This project will help you implement video content delivery with token-based authentication, which ensures secure and time-limited access to video streams.

---

### **Table of Contents**

- [Introduction](#introduction)
- [Features](#features)
- [Tutorial: Building the System](#tutorial-building-the-system)
    - [Step 1: Set up the Spring Boot Project](#step-1-set-up-the-spring-boot-project)
    - [Step 2: Create Video Management Endpoints](#step-2-create-video-management-endpoints)
    - [Step 3: Generate and Validate Signed URLs](#step-3-generate-and-validate-signed-urls)
    - [Step 4: Secure Video Streaming](#step-4-secure-video-streaming)
- [API Endpoints](#api-endpoints)
- [License](#license)
- [Disclaimer](#disclaimer)

---

### **Introduction**

This tutorial demonstrates how to build a secure video streaming system with features similar to YouTube. By following the steps outlined here, you will learn how to stream videos securely using signed URLs that prevent unauthorized access and control access based on the client's IP address.

In this tutorial, you'll use Spring Boot to:
- Serve video files dynamically.
- Create secure signed URLs for accessing videos.
- Ensure that video URLs are only valid for a limited time.
- Restrict video access by IP address.

### **Features**

- **Secure Video Streaming**: Each video stream is protected by signed URLs to ensure only authorized users can access them.
- **IP-based Restrictions**: Access to video content is restricted based on the client’s IP address, ensuring more secure streaming.
- **Dynamic URL Generation**: URLs are dynamically generated with tokens that expire after a set period.
- **Simple Video Management**: List and stream videos easily from a local directory.

---

### **Tutorial: Building the System**

#### **Step 1: Set up the Spring Boot Project**

1. Clone the repository:

   ```bash
   git clone https://github.com/KHAOUITI-Apps/CDN.git
   cd CDN
   ```

2. Build and run the project using Maven:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   This will start the Spring Boot application on `http://localhost:8080`.

---

#### **Step 2: Create Video Management Endpoints**

We need to create an endpoint that lists all available videos and provides signed URLs for secure access.

1. Create a directory named `videos` to store video files in `.mp4` format.
2. The `VideoController` class contains a method `getAllVideos()` that retrieves all video files from the directory and generates signed URLs for each video.

---

#### **Step 3: Generate and Validate Signed URLs**

The key part of this tutorial is to generate signed URLs for videos, which will allow users to stream videos only if they have the correct URL token.

1. The `URLSigner` class contains logic to generate signed URLs for videos using HMAC-SHA256 hashing.
2. The URLs are valid only for a limited time (2 hours in this example) and are tied to the client’s IP address.

---

#### **Step 4: Secure Video Streaming**

To stream videos, you'll create an endpoint `/video/stream/{videoId}` that checks the validity of the signed URL.

1. The server checks if the video exists.
2. It then validates the token, expiration time, and IP address.
3. If the validation passes, the video is streamed to the client.

---

### **API Endpoints**

#### `GET /video/all`

This endpoint returns a list of all available videos with their signed URLs.

**Example Response:**

```json
[
    {
        "videoId": "mother",
        "signedUrl": "/video/stream/mother?token=l6dH_EcBHRKgseE-OdvRJVqu4oTzhhEtXYcVXNpGtP4&expires=1738371232&ip=0:0:0:0:0:0:0:1"
    },
    {
        "videoId": "palestine",
        "signedUrl": "/video/stream/palestine?token=MdHGmKerD-9lPb_bBLsiMBmXlr9EL1ID8Ssbf-o4mp0&expires=1738371232&ip=0:0:0:0:0:0:0:1"
    },
    {
        "videoId": "quran",
        "signedUrl": "/video/stream/quran?token=Wn7jfkAoTv69e7MYCJM9qOKKmR_DY9XlW5JJAXY5vqQ&expires=1738371232&ip=0:0:0:0:0:0:0:1"
    }
]
```

#### `GET /video/stream/{videoId}`

This endpoint streams a video by validating the signed URL.

**Parameters:**

- `videoId` - The ID of the video you want to stream.
- `token` - The signed token that authorizes access to the video.
- `expires` - The expiration timestamp of the signed URL.
- `ip` - The client's IP address.

If the URL is valid, the video will be streamed. If not, a `403 Forbidden` or `404 Not Found` response will be returned.

---

### **License**

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

### **Disclaimer**

By following this tutorial and using this code, you agree not to redistribute or use it in unauthorized ways. If you wish to modify or distribute this code, please contact the author for permission.

For further queries or permission requests, feel free to reach out to me on [LinkedIn](https://www.linkedin.com/in/khaouitiabdelhakim/).

