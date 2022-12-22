package day7;

import java.util.ArrayList;
import java.util.List;

public class ElfDrive{
    public static TreeNode pwd;
    public static List<TreeNode> nodes = new ArrayList<>();

    public ElfDrive(){
        // Create a 'root' directory by default
        pwd = new ElfDir();
    }

    // TODO:  Perform comparison on absPath between nodes
    public static TreeNode getNodeInPwd(String name) throws RuntimeException{
        for(TreeNode n: nodes){
            if (n.parentNode == pwd && n.name.equalsIgnoreCase(name)){
                System.out.println("Parent node: " + n.parentNode + " nodeName: " + n.name);
                return n;
            }
        }
        throw new RuntimeException("Node not found: " + name);
    }

    public static boolean nodeExists(String name) {
        for (TreeNode n : nodes) {
            if (n.parentNode == pwd && n.name.equalsIgnoreCase(name)) {
                System.out.println("Node " + name + " already exists!");
                return true;
            }
        }
        return false;
    }


    public static TreeNode changeDirFromPwd(String name){
        if(name.equals("..")) {
            pwd = pwd.parentNode;
            return pwd;
        } else if (name.equals("/")){
            name = "root";
        }

        TreeNode newNode;
        try{
            newNode = getNodeInPwd(name);
        } catch (RuntimeException e){
            System.out.println("In catch of changeDirFromPwd...name: " + name);
            // if node doesn't exist create it
            newNode = new ElfDir(name, pwd, name);
        }
        pwd = newNode;
        System.out.println("New Node is: " + newNode);
        System.out.println("After changing to " + name + " Current pwd is " + pwd);
        return newNode;
    }


    public static TreeNode getNodeInPwd(String name, Integer size){
        if(nodeExists(name)) {
            System.out.println("Node " + name + " already exists!!!!");
            return getNodeInPwd(name);
        }

        if (size>0){
            return new ElfFile(name, pwd, size, name);
        } else{
//            System.out.println("Creating dir " + name + " in " + pwd );
            return new ElfDir(name, pwd, name);
        }

    }

    public static TreeNode getNodeGlobal(String absPath){
        for (TreeNode n: nodes){
            if(n.getAbsPath().equalsIgnoreCase(absPath)){
                return n;
            }
        }
        throw new RuntimeException("Node not found: " + absPath);
    }



    public String toString(){
        return "Num nodes " + nodes.size() + " with pwd=" + pwd.getAbsPath();
    }
}
