package Random.Decorater;
/*
abstract class SimpleFileStream{
    public abstract void writeText( String text);
    //cost
}
abstract class Decorater extends SimpleFileStream{

}
class CompressionDecorator extends Decorater{
    Decorater decorater;
    public CompressionDecorator(Decorater decorater){
        this.decorater = decorater;
    }
    @Override
    public void writeText(String text) {
        System.out.println("write your Compressed text here");
    }
    //cost
}
class EncryptionDecorator extends Decorater{

//    Decorater decorater;
//    public EncryptionDecorator(Decorater decorater){
//        this.decorater = decorater;
//    }
    @Override
    public void writeText(String text) {
        System.out.println("write your Encrypted text here");
    }
    //cost
}
public class FileStream {
    Decorater decorater = new CompressionDecorator(new EncryptionDecorator());

}
*/
//component
abstract class FileStream {
    public abstract void write(String text);
}
//Concrete component
class SimpleFileStream extends FileStream {
    @Override
    public void write(String text) {
        System.out.println("writing to Disk: " + text);
    }
}
// The Decorater
abstract class FileDecorater extends FileStream {
    protected FileStream wrappedStream;
    public FileDecorater(FileStream wrappedStream) {
        this.wrappedStream = wrappedStream;
    }
}

//concrete Decorater
class CompressionDecorator extends FileDecorater {
    public CompressionDecorator(FileStream wrappedStream) {
        super (wrappedStream);
    }

    @Override
    public void write(String text) {
        String compressed = "[Compressed] "+ text;
        wrappedStream.write(compressed);
    }
}
class EncryptionDecorator extends FileDecorater {
    public EncryptionDecorator(FileStream wrappedStream) {
        super (wrappedStream);
    }
    @Override
    public void write(String text) {
        String encrypted = "[Encrypted] "+ text;
        wrappedStream.write(encrypted);
    }
}
/*
Q: What happens if the order is changed?

Answer: The pattern still works, but the result changes. Encrypted(Compressed(Text)) is different from Compressed(Encrypted(Text)). In an interview, mention that the order of decoration matters if the operations are not commutative.

Q: How to handle "Add-only-once" decorators?

SDE-2 Answer: Use a check in the constructor. You can use instanceof or a custom flag inside the object to see if a CompressionDecorator already exists in the chain.
 */