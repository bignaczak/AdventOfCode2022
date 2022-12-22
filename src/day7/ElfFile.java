package day7;

public class ElfFile extends TreeNode{

    public ElfFile(String name, TreeNode parent, int size, String pathPart){
        super(name, parent, pathPart);
        this.size = size;
        System.out.println("Just created File: " + this + " with parentNode: " + this.parentNode +
                " and path: " + this.getAbsPath());
    }
}
