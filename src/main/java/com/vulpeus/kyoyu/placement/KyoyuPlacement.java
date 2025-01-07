package com.vulpeus.kyoyu.placement;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vulpeus.kyoyu.Kyoyu;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class KyoyuPlacement {

    private final UUID uuid;
    private final KyoyuRegion region;
    private final List<KyoyuRegion> subRegions;
    private final String ownerName;
    private String updaterName;
    private final String filename;
    private transient File file = null;

    public KyoyuPlacement(UUID uuid, KyoyuRegion region, List<KyoyuRegion> subRegions, String ownerName, String updaterName, File file) {
        this.uuid = uuid;
        this.region = region;
        this.subRegions = subRegions;
        this.ownerName = ownerName;
        this.updaterName = updaterName;
        this.file = file;
        String filename = null;
        try {
            String rawFilename = file.getName();
            byte[] data = this.readFromFile();
            filename = getFileHash(data) + rawFilename.substring(rawFilename.lastIndexOf('.'));
            this.file = getFileFromFilename(filename);
            writeToFile(data);
        } catch (IOException e) {
            Kyoyu.LOGGER.error(e);
        }
        this.filename = filename;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return this.region.getName();
    }

    public List<KyoyuRegion> getSubRegions() {
        return subRegions;
    }

    public KyoyuRegion getRegion() {
        return region;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void updateBy(String updaterName) {
        this.updaterName = updaterName;
    }

    public String getFilename() { return filename; }

    public File getFile() {
        return file;
    }

    public boolean existFile() {
        return file != null && file.exists();
    }

    private static final Type listType = new TypeToken<List<KyoyuPlacement>>() {}.getType();
    public static List<KyoyuPlacement> fromJsonList(String json) {
        Gson gson = new Gson();
        List<KyoyuPlacement> kyoyuPlacementList = gson.fromJson(json, listType);
        for (KyoyuPlacement kyoyuPlacement: kyoyuPlacementList) {
            kyoyuPlacement.file = getFileFromFilename(kyoyuPlacement.getFilename());
        }
        return kyoyuPlacementList;
    }

    public static String toJsonList(List<KyoyuPlacement> kyoyuPlacementList) {
        List<KyoyuPlacement> target = new LinkedList<KyoyuPlacement>(kyoyuPlacementList);
        Gson gson = new Gson();
        return gson.toJson(target, listType);
    }

    public static KyoyuPlacement fromJson(String json) {
        Gson gson = new Gson();
        KyoyuPlacement kyoyuPlacement = gson.fromJson(json, KyoyuPlacement.class);
        kyoyuPlacement.file = getFileFromFilename(kyoyuPlacement.getFilename());
        return kyoyuPlacement;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    private static File getFileFromFilename(String filename) {
        return Kyoyu.getSaveSchemeDir().resolve(filename).toFile();
    }

    public byte[] readFromFile() throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        return IOUtils.toByteArray(inputStream);
    }

    public void writeToFile(byte[] byteArray) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFileHash(byte[] data) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md2 = MessageDigest.getInstance("MD2");
            byte[] hash = md2.digest(data);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
        } catch (Exception ignore) {}
        return sb.toString();
    }
}
