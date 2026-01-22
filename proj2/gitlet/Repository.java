package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The commits directory. */
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    /** The blobs directory. */
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    /** The branches directory. */
    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");
    private String head;
    private String activatedBranchName;
    /** The staging area directory. */
    /* TODO: fill in the rest of this class. */

    public Repository() {
        if (GITLET_DIR.exists()) {
            File repository = join(GITLET_DIR, "repo");
            if (repository.exists()) {
                Repository repo = Utils.readObject(repository, Repository.class);
                this.head = repo.head;
                this.activatedBranchName = repo.activatedBranchName;
            }
        }
    }

    public void saveRepository() {
        File repository = join(GITLET_DIR, "repo");
        Utils.writeObject(repository, this);
    }

    public void init() {
        //Initialize the basic directories
        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        BRANCHES_DIR.mkdir();
        Commit initCommit = new Commit();
        initCommit.saveCommit();
        Branch branch = new Branch(initCommit.getCommitID(), "master");
        branch.saveBranch();
        this.head = initCommit.getCommitID();
        this.activatedBranchName = "master";
        this.saveRepository();
    }

    public void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            System.err.println("File does not exist.");
            System.exit(0);
        }

        StagingArea stagingArea = new StagingArea();
        stagingArea.addFile(fileName, head);
        stagingArea.saveStagingArea();
    }

    public void commit(String message) {
        StagingArea stagingArea = new StagingArea();
        if (stagingArea.getStageForAdd().isEmpty() && stagingArea.getStageForRemove().isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        Commit currentCommit = Commit.fromFile(head);
        Map<String, String> newMapFileNameToBlob = currentCommit.mapFileNameToBlob;
        for(String fileToBeAddedOrModeified : stagingArea.getStageForAdd().keySet()) {
            newMapFileNameToBlob.put(fileToBeAddedOrModeified, stagingArea.getStageForAdd().get(fileToBeAddedOrModeified));
        }

        Commit newCommit = new Commit(message, head, null, newMapFileNameToBlob);
        newCommit.saveCommit();

        head = newCommit.getCommitID();

        Branch currentBranch = Branch.fromFile(activatedBranchName);
        currentBranch.updateBranch(newCommit.getCommitID());

        stagingArea.clear();
        stagingArea.saveStagingArea();

        saveRepository();
    }

    public void remove(String fileName) {
        StagingArea stagingArea = new StagingArea();
        Commit currentCommit = Commit.fromFile(head);
        if (!currentCommit.mapFileNameToBlob.containsKey(fileName) && !stagingArea.getStageForAdd().containsKey(fileName)) {
            Utils.message("No reason to remove file.");
        }

        stagingArea.removeFile(fileName, head);
        stagingArea.saveStagingArea();
    }

    public void logPrint() {
        String commitLog = Commit.getCurrentCommitLog(head);
        System.out.println(commitLog);
    }

    public void globalCommitLogPrint() {
        String commitGlobalLog = Commit.getGlobalCommitLog();
        System.out.println(commitGlobalLog);

    }

    public void commitWithMessagePrint(String message) {
        List<String> commitWithMessage = Commit.findCommitLogWithMessage(message);
        System.out.println(commitWithMessage);
    }

    public void statusPrint() {
        StagingArea stagingArea = new StagingArea();
        System.out.println("=== Branches ===");
        for(String branchName : Utils.plainFilenamesIn(BRANCHES_DIR)) {
            if(branchName.equals(activatedBranchName)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        stagingArea.printStagedFileInOrder();
        System.out.println();

        System.out.println("=== Removed Files ===");
        stagingArea.printRemovedFilesInOrder();
        System.out.println();
    }

    public void checkoutWithFileName(String fileName) {
        checkoutWithCommitAndFileName(head, fileName);
    }

    public void checkoutWithCommitAndFileName(String commitID, String fileName) {
        Commit commitToCheckout = Commit.fromFile(commitID);
        File fileToCheckout = join(CWD, fileName);

        if(commitToCheckout == null) {
            System.out.println("No commit with that id exists.");
            return;
        }

        Map<String, String> mapFileNameToBlob = commitToCheckout.mapFileNameToBlob;
        if(!mapFileNameToBlob.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        Blob requiredBlob= Blob.fromFile(mapFileNameToBlob.get(fileName));
        Utils.writeContents(fileToCheckout, requiredBlob.getFileContent());

    }

    private static void checkUntrackedFiles(Commit current, Commit target) {
        List<String> cwdFiles = Utils.plainFilenamesIn(CWD);
        for (String fileName : cwdFiles) {
            if (!current.mapFileNameToBlob.containsKey(fileName) && target.mapFileNameToBlob.containsKey(fileName)) {
                throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
            }
        }
    }

    private static void checkoutWithCommitID(String currentCommitID, String targetCommitID) {
        Commit currentCommit = Commit.fromFile(currentCommitID);
        Commit targetCommit = Commit.fromFile(targetCommitID);

        checkUntrackedFiles(currentCommit, targetCommit);

        for (String fileName : targetCommit.mapFileNameToBlob.keySet()) {
            String blobID = targetCommit.mapFileNameToBlob.get(fileName);
            Utils.writeContents(join(CWD, fileName), Blob.fromFile(blobID).getFileContent());
        }

        for (String fileName : currentCommit.mapFileNameToBlob.keySet()) {
            if (!targetCommit.mapFileNameToBlob.containsKey(fileName)) {
                Utils.restrictedDelete(fileName);
            }
        }

        StagingArea stagingArea = new StagingArea();
        stagingArea.clear();
        stagingArea.saveStagingArea();
    }

    private void checkoutHelp(String branchName) {

        if (!Utils.plainFilenamesIn(BRANCHES_DIR).contains(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }

        if(branchName.equals(activatedBranchName)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        Branch branchToCheckout = Branch.fromFile(branchName);

        checkoutWithCommitID(head, branchToCheckout.getCommitID());

        activatedBranchName = branchName;
        head = branchToCheckout.getCommitID();
        saveRepository();
    }

    public void checkoutWithBranchName(String branchName) {
        try {
            checkoutHelp(branchName);
        } catch (GitletException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void branch(String branchName) {
        List<String> branches = Utils.plainFilenamesIn(BRANCHES_DIR);
        if(branches.contains(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        Branch newBranch = new Branch(head, branchName);
        newBranch.saveBranch();
        saveRepository();
    }

    public void branchRemove(String branchName) {
        if (branchName.equals(activatedBranchName)) {
            System.out.println("Cannot remove the current branch.");
        }
        List<String> branches = Utils.plainFilenamesIn(BRANCHES_DIR);
        if (!branches.contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        File branchFile = join(BRANCHES_DIR, branchName);
        branchFile.delete();
    }

    private void resetHelp(String commitID) {
        if (!Utils.plainFilenamesIn(COMMITS_DIR).contains(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }

        checkoutWithCommitID(head, commitID);

        head = commitID;
        Branch activeBranch = Branch.fromFile(activatedBranchName);
        activeBranch.updateBranch(commitID);
        activeBranch.saveBranch();

        saveRepository();
    }

    public void reset(String commitID) {
        try {
            resetHelp(commitID);
        } catch (GitletException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void mergeBranch(String branchName) {
        Branch branchToMerge = Branch.fromFile(branchName);
        Commit commitOfBranch = Commit.fromFile(branchToMerge.getCommitID());
        Commit currentCommit = Commit.fromFile(head);
        if(commitOfBranch.isAncestor(head)) {
            return;
        }

        if (currentCommit.isAncestor(branchToMerge.getCommitID())) {
            head = branchToMerge.getCommitID();
            Branch currentBranch = Branch.fromFile(activatedBranchName);
            currentBranch.updateBranch(branchToMerge.getCommitID());
            currentBranch.saveBranch();
            saveRepository();
            return;
        }

        //find the split point
    }

    private String findSplitPoint(String branchName) {
        Branch branch = Branch.fromFile(branchName);
        return null;
    }

}
