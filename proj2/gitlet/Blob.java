package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private String blobID;
    private String fileContent;

    Blob(String content) {
        this.fileContent = content;
        this.blobID = Utils.sha1(content);
    }

    public String getBlobID() {
        return blobID;
    }

    public String getFileContent() {
        return fileContent;
    }

    public static Blob fromFile(String requiredBlobID) {
        File blobFile = Utils.join(Repository.BLOBS_DIR, requiredBlobID);
        if (!blobFile.exists()) {
            return null;
        }
        return Utils.readObject(blobFile, Blob.class);
    }

    public void saveBlob() {
        File blobFile = Utils.join(Repository.BLOBS_DIR, this.blobID);
        Utils.writeObject(blobFile, this);
    }
}
