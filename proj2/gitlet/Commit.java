package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timeStamp;
    private String parent1;
    private String parent2;
    Map<String, String> mapFileNameToBlob= new HashMap<>();
    private String commitID;

    /* TODO: fill in the rest of this class. */
    public Commit() {
        this.message = "initial commit";
        this.timeStamp = new Date(0);
        this.parent1 = null;
        this.parent2 = null;
        this.commitID = Utils.sha1(Utils.serialize(this));
    }

    public Commit(String message, String parent1, String parent2, Map<String, String> mapFileNameToBlob) {
        this.message = message;
        this.timeStamp = new Date();
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.mapFileNameToBlob = mapFileNameToBlob;
        this.commitID = Utils.sha1(Utils.serialize(this));
    }

    public String getCommitID() {
        return commitID;
    }


    public String getParent1() {
        return parent1;
    }

    public void saveCommit() {
        File file =  Utils.join(Repository.COMMITS_DIR, this.commitID);
        Utils.writeObject(file, this);
    }

    public static Commit fromFile(String commitID) {
        File file =  Utils.join(Repository.COMMITS_DIR, commitID);
        if(!file.exists()) {
            return null;
        }

        return Utils.readObject(file, Commit.class);
    }

    public String toString() {
        String s = "===" + "\n";
        s += "commit: " + commitID + "\n";
        SimpleDateFormat  sdf = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z", Locale.ENGLISH);
        s += "Date: " + sdf.format(timeStamp) + "\n";
        s += message + "\n";
        return s;
    }

    public static String getCurrentCommitLog(String currentCommitID) {
        StringBuilder log = new StringBuilder();
        String commitID = currentCommitID;
        while(commitID != null) {
            Commit commitToBePresented = fromFile(commitID);
            log.append(commitToBePresented.toString()).append("\n");
            commitID = commitToBePresented.parent1;
        }
        return log.substring(0, log.length()-1);
    }

    public static String getGlobalCommitLog() {
        List<String> globalCommits = Utils.plainFilenamesIn(Repository.COMMITS_DIR);
        StringBuilder globalLog = new StringBuilder();
        for(String commitID : globalCommits) {
            Commit commitToBePresented = fromFile(commitID);
            globalLog.append(commitToBePresented.toString()).append("\n");
        }
        return globalLog.substring(0, globalLog.length()-1);
    }

    public static List<String> findCommitLogWithMessage(String message) {
        List<String> allCommits = Utils.plainFilenamesIn(Repository.COMMITS_DIR);
        List<String> commitWithMessage = new LinkedList<>();
        for(String commitID : allCommits) {
            Commit current = fromFile(commitID);
            if(current.message.equals(message)) {
                commitWithMessage.add(current.message);
            }
        }
        return commitWithMessage;
    }

    public boolean isAncestor(String commitID) {
        if(parent1 == null) {
            return false;
        }

        if(commitID.equals(this.commitID)) {
            return true;
        }
        Commit parent1Commit = fromFile(parent1);
        boolean ancestorFound = parent1Commit.isAncestor(commitID);
        if(parent1 != null) {
            Commit parent2Commit = fromFile(parent2);
            ancestorFound = ancestorFound || parent2Commit.isAncestor(commitID);
        }
        return ancestorFound;
    }

}
