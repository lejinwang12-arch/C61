package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.join;

public class StagingArea implements Serializable {
    private Map<String, String> stageForAdd;
    private Set<String> stageForRemove;

    public StagingArea() {
       if (Repository.GITLET_DIR.exists()) {
           File STAGE_FILE = Utils.join(Repository.GITLET_DIR, "stagingArea");
           if(STAGE_FILE.exists()) {
               StagingArea sta = Utils.readObject(STAGE_FILE, StagingArea.class);
               stageForAdd = sta.stageForAdd;
               stageForRemove = sta.stageForRemove;
           } else {
               stageForAdd = new HashMap<>();
               stageForRemove = new HashSet<>();
           }
       } else {
           System.exit(0);
       }
    }

    public void addFile(String fileName, String currentCommitID) {
        File file = new File(fileName);
        String fileContent = Utils.readContentsAsString(file);

        Commit currentCommit = Commit.fromFile(currentCommitID);
        Map<String,String> mapFileNameToBlobIDInCurrentCmmit = currentCommit.getMapFileNameToBlob();

        if(mapFileNameToBlobIDInCurrentCmmit.containsKey(fileName)) {
            String oldBlobID = mapFileNameToBlobIDInCurrentCmmit.get(fileName);
            Blob oldBlob = Blob.fromFile(oldBlobID);
            String oldFileContent = oldBlob.getFileContent();

            if(oldFileContent.equals(fileContent)) {
                return;
            } else {
                Blob newBlob = new Blob(fileContent);
                stageForAdd.put(fileName, newBlob.getBlobID());
                newBlob.saveBlob();
            }
        }
    }

    public void saveStagingArea() {
        File saveFile = Utils.join(Repository.GITLET_DIR, "stagingArea");
        Utils.writeObject(saveFile, this);
    }

    public Map<String, String> getStageForAdd() {
        return stageForAdd;
    }

    public Set<String> getStageForRemove() {
        return stageForRemove;
    }

    public void clear() {
        stageForAdd.clear();
        stageForRemove.clear();
    }

    public void removeFile(String fileName, String currentCommitID) {
        File file = Utils.join(Repository.CWD, fileName);
        Commit currentCommit = Commit.fromFile(currentCommitID);
        if (currentCommit.getMapFileNameToBlob().containsKey(fileName)) {
            stageForRemove.add(fileName);
            if (file.exists()) {
                file.delete();
            }
        }
        if(stageForAdd.containsKey(fileName)) {
            stageForAdd.remove(fileName);
        }
    }

    public void printStagedFileInOrder() {
        String[] stagedFiles = stageForAdd.keySet().toArray(new String[0]);
        Arrays.sort(stagedFiles);
        for (String stagedFile : stagedFiles) {
            System.out.println(stagedFile);
        }
    }

    public void printRemovedFilesInOrder() {
        String[] removedFiles = stageForRemove.toArray(new String[0]);
        Arrays.sort(removedFiles);
        for (String removedFile : removedFiles) {
            System.out.println(removedFile);
        }
    }

}
