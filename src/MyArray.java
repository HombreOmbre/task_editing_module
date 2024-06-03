import java.util.Iterator;

public class MyArray<T>
    implements Iterable<T> {

    private T[] arrayDesc;
    private int count;

    public MyArray(int size) {
        this.arrayDesc = (T[]) new Object[size];
        this.count = 0;
    }

    public MyArray(MyArray<T> oldArr) {
        this.arrayDesc = (T[]) new Object[oldArr.getSize()];
        this.count = oldArr.getSize();
        for (int i = 0; i < oldArr.getSize(); i++) {
            this.arrayDesc[i] = oldArr.get(i);
        }
    }

    public int getSize() {
        return count;
    }

    public void add(T val) {
        if (this.count >= arrayDesc.length) {
            T[] tmpArr = (T[]) new Object[count * 2];

            for (int i = 0; i < this.arrayDesc.length; i++) {
                tmpArr[i] = this.arrayDesc[i];
            }

            this.arrayDesc = tmpArr;
        }
        this.arrayDesc[count++] = val;
    }

    public T get(int index) {
        if (index < this.count) {
            return this.arrayDesc[index];
        }

        throw new IllegalArgumentException();
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {

            int currElem = 0;

            @Override
            public boolean hasNext() {
                return currElem < count;
            }

            @Override
            public T next() {
                return arrayDesc[currElem++];
            }
        };
    }
}
