package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }

        Repository repository = new Repository();
        String firstArg = args[0];

        if (!args[0].equals("init") && !Repository.GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        switch(firstArg) {
            case "init":
                validateNumAndFormatArgs(args, 1);
                repository.init();
                break;
            case "add":
                validateNumAndFormatArgs(args, 2);
                repository.add(args[1]);
                break;
            case "commit":
                validateNumAndFormatArgs(args, 2);
                repository.commit(args[1]);
                break;
            case "rm":
                validateNumAndFormatArgs(args, 2);
                repository.remove(args[1]);
                break;
            case "log":
                validateNumAndFormatArgs(args, 1);
                repository.logPrint();
                break;
            case "global-log":
                validateNumAndFormatArgs(args, 1);
                repository.globalCommitLogPrint();
                break;
            case "find":
                validateNumAndFormatArgs(args, 2);
                repository.commitWithMessagePrint(args[1]);
                break;
            case "status":
                validateNumAndFormatArgs(args, 1);
                repository.statusPrint();
                break;
            case "checkout":
                handleCheckoutArgs(args, repository);
                break;
            case "branch":
                validateNumAndFormatArgs(args, 2);
                repository.branch(args[1]);
                break;
            case "rm-branch":
                validateNumAndFormatArgs(args, 2);
                repository.branchRemove(args[1]);
                break;
            case "reset":
                validateNumAndFormatArgs(args, 2);
                repository.reset(args[1]);
                break;
            case "merge":
                validateNumAndFormatArgs(args, 2);
                repository.mergeBranch(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    private static void handleCheckoutArgs(String[] args, Repository repository) {
        if (args.length == 3) {
            // java gitlet.Main checkout -- [file name]
            validateNumAndFormatArgs(args, 3);
            String fileName = args[2];
            repository.checkoutWithFileName(fileName);
        } else if (args.length == 4) {
            //java gitlet.Main checkout [commit id] -- [file name]
            validateNumAndFormatArgs(args, 4);
            String commitID = args[1];
            String fileName = args[3];
            repository.checkoutWithCommitAndFileName(commitID, fileName);
        } else if (args.length == 2) {
            //java gitlet.Main checkout [branch name]
            validateNumAndFormatArgs(args, 2);
            String branchName = args[1];
            repository.checkoutWithBranchName(branchName);
        } else {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void validateNumAndFormatArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }

        if (args[0].equals("checkout")) {
            if (args.length == 3 && !args[1].equals("--")) {
                // java gitlet.Main checkout -- [file name]
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            if (args.length == 4 && !args[2].equals("--")) {
                // java gitlet.Main checkout [commit id] -- [file name]
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
        }
    }
}
