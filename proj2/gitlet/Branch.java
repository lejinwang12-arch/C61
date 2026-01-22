package gitlet;

import java.io.File;
import java.io.Serializable;

public class Branch implements Serializable {
    private String name;
    private String commitID;

    Branch(String commitID, String name)
    {
        this.commitID = commitID;
        this.name = name;
    }

    public String getCommitID() {
        return commitID;
    }

    public String getName() {
        return name;
    }

    public void saveBranch() {
        File file = Utils.join(Repository.BRANCHES_DIR, this.name);
        Utils.writeObject(file, this);
    }

    public static Branch fromFile(String branchName) {
        File file = Utils.join(Repository.BRANCHES_DIR, branchName);
        if (!file.exists()) {
            return null;
        }
        Branch branch = Utils.readObject(file, Branch.class);
        return branch;
    }

    public void updateBranch(String commitID) {
        this.commitID = commitID;
    }

}
