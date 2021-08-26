package com.hacknife.betapublish

import org.gradle.api.DefaultTask
import org.gradle.internal.impldep.com.google.gson.Gson


class BaseTask extends DefaultTask {

    static void upload(Map<String, String> map, File file, Closure percentClosure, Closure completeClosure) {
        String url = "https://www.pgyer.com/apiv2/app/upload"
        String BOUNDARY = "----WebKitFormBoundaryDwvXSRMl0TBsL6kW"
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection()
        connection.setConnectTimeout(3 * 60 * 1000)
        connection.setReadTimeout(3 * 60 * 1000)
        connection.setRequestMethod("POST")
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY)
        connection.setDoOutput(true)
        OutputStream out = new DataOutputStream(connection.getOutputStream())
        byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes()

        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                StringBuilder sb = new StringBuilder()
                sb.append("--")
                sb.append(BOUNDARY)
                sb.append("\r\n")
                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"")
                sb.append("\r\n")
                sb.append("\r\n")
                sb.append(entry.getValue())
                sb.append("\r\n")
                byte[] data = sb.toString().getBytes()
                out.write(data)
            }
        }

        if (file != null) {
            String fileName = "file"
            StringBuilder sb = new StringBuilder()
            sb.append("--")
            sb.append(BOUNDARY)
            sb.append("\r\n")
            sb.append("Content-Disposition: form-data;name=\"" + fileName + "\";filename=\"" + file.getName() + "\"\r\n")
            sb.append("Content-Type: application/vnd.android.package-archive\r\n\r\n")
            byte[] data = sb.toString().getBytes()
            out.write(data)
            DataInputStream ins = new DataInputStream(new FileInputStream(file))
            int bytes = 0
            float percentUpload = 0f
            float contentLength = file.size()
            float sendLength = 0f
            byte[] bufferOut = new byte[1024]
            while ((bytes = ins.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes)
                sendLength += bytes
                float percent = sendLength / contentLength
                if (percent - percentUpload > 0.01f) {
                    percentUpload = percent
                    percentClosure("upload file percent: " + String.valueOf(percentUpload))
                }
            }
            out.write("\r\n".getBytes())
            ins.close()
        }

        out.write(end_data)
        out.flush()
        out.close()

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inStream = connection.getInputStream()
            byte[] number = read(inStream)
            String json = new String(number)
            completeClosure(json)
        }
    }

    static byte[] read(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}