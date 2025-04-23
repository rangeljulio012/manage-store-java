package com.manage_store_api.main.Utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryUploadUtil {

    private final Cloudinary cloudinary;

    public CloudinaryUploadUtil() {
        // Configuración de Cloudinary (puedes mover esto a application.properties)
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dgj7gbzlp",
                "api_key", "699489229259297",
                "api_secret", "6pHjqGLRJkUcGjNlpvVz2gI1guc",
                "secure", true
        ));
    }

    /**
     * Sube un archivo a Cloudinary
     * @param file Archivo a subir
     * @return Mapa con la información de la imagen subida
     * @throws IOException Si ocurre un error al subir el archivo
     */
    public Map<String, Object> uploadFile(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "auto", // Detecta automáticamente si es imagen, video, etc.
                "use_filename", true,
                "unique_filename", true,
                "overwrite", true
        ));
    }

    /**
     * Sube un archivo a Cloudinary con un nombre específico
     * @param file Archivo a subir
     * @param publicId Nombre público del archivo en Cloudinary
     * @return Mapa con la información de la imagen subida
     * @throws IOException Si ocurre un error al subir el archivo
     */
    public Map<String, Object> uploadFile(MultipartFile file, String publicId) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", publicId,
                "resource_type", "auto",
                "overwrite", true
        ));
    }

    /**
     * Elimina un archivo de Cloudinary
     * @param publicId ID público del archivo en Cloudinary
     * @return Mapa con el resultado de la operación
     * @throws IOException Si ocurre un error al eliminar el archivo
     */
    public Map<String, Object> deleteFile(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    /**
     * Obtiene la URL pública de un archivo
     * @param publicId ID público del archivo en Cloudinary
     * @return URL pública del archivo
     */
    public String getFileUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }
}