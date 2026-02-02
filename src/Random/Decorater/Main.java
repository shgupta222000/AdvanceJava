package Random.Decorater;

public class Main {
    public static void main(String[] args) {
        FileStream myStream = new EncryptionDecorator(
                new CompressionDecorator(
                        new SimpleFileStream()
                )
        );

        myStream.write("Hello World");
// Output: Writing to disk: [Encrypted] [Compressed] Hello World
    }
}
