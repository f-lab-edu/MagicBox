package kr.magicbox.media.application.port.out;

public interface ObjectStoragePort {
    String generatePresignedPutUrl(String uuid, String contentType, long ttlSeconds);
    void deleteIfExists(String uuid);
}
