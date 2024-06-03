import java.util.Arrays;
import java.util.Iterator;

public class StringDesc
    extends DescMethods {
    private MyArray<String> stringList;
    private MyArray<ImgDesc> imgList;

    public StringDesc(String txtDesc) {
        this.stringList = new MyArray<>(8);
        this.imgList = new MyArray<>(8);
        this.addDescToStringList(txtDesc);
    }

    private void addDescToStringList(String txtDesc) {
        Arrays.stream(txtDesc.split("[ ]"))
                .map(
                        word -> {
                            if (word.startsWith("[\\href]")) {
                                imgList.add(new ImgDesc(word));
                                word = "IMG " + (imgList.getSize() - 1);
                            }

                            if (word.equals("←")) {
                                word += "\n";
                            }

                            return word;
                        }
                ).forEach(stringList::add);
    }

    @Override
    public String getDescValue() {
        int i = 0;
        int arrSize = stringList.getSize();
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = stringList.iterator();

        while (iterator.hasNext()) {
            String word = iterator.next();
            if (word.startsWith("IMG")) {
                word = "\\" + imgList.get(Integer.parseInt(word.split(" ")[1])).getDescValue();
            }

            if (i < arrSize - 2 || (!word.equals("\\n"))) {
                word += " ";
            }


            sb.append(word);

            i++;
        }

        return sb.toString().trim();
    }
}
