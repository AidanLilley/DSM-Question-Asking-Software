public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {

        this.doubElements = _elements;
    }

    public double getElementatIndex(int _index) {

        if(_index > -1 && _index < this.doubElements.length) {
            return this.doubElements[_index];
        }
        return -1;
    }

    public void setElementatIndex(double _value, int _index) {

        if(_index > -1 && _index < this.doubElements.length) {
            this.doubElements[_index] = _value;
        } else if(this.doubElements.length >= 1) {
            this.doubElements[this.doubElements.length-1] = _value;
        }
    }

    public double[] getAllElements() {

        return this.doubElements;
    }

    public int getVectorSize() {

        return this.doubElements.length;
    }

    public Vector reSize(int _size) {

        if(_size == this.doubElements.length || _size <= 0) {
            return this;
        }
        double[] temp = new double[_size];
        for (int i=0; i<this.doubElements.length && i<_size;i++) {
            temp[i] = this.doubElements[i];
        }
        if (_size > this.doubElements.length) {
            for (int i=this.doubElements.length;i<_size;i++) {
                temp[i] = -1.0;
            }
        }
        Vector vTemp = new Vector(temp);
        return vTemp;
    }

    public Vector add(Vector _v) {

        Vector vTemp = this;
        if(_v.doubElements.length != vTemp.doubElements.length) {
            if(_v.doubElements.length > vTemp.doubElements.length) {
                vTemp = vTemp.reSize(_v.doubElements.length);
            } else
                _v = _v.reSize(vTemp.doubElements.length);
        }
        double[] temp2 = new double[vTemp.doubElements.length];
        Vector vTemp2 = new Vector(temp2);
        for(int i=0;i<vTemp.doubElements.length;i++){
            temp2[i] = vTemp.doubElements[i] + _v.doubElements[i];
        }
        vTemp2.doubElements = temp2;
        return vTemp2;
    }

    public Vector subtraction(Vector _v) {

        Vector vTemp = this;
        if(_v.doubElements.length != vTemp.doubElements.length) {
            if(_v.doubElements.length > vTemp.doubElements.length) {
                vTemp = vTemp.reSize(_v.doubElements.length);
            } else
                _v = _v.reSize(vTemp.doubElements.length);
        }
        double[] temp2 = new double[vTemp.doubElements.length];
        Vector vTemp2 = new Vector(temp2);
        for(int i=0;i<vTemp.doubElements.length;i++){
            temp2[i] = vTemp.doubElements[i] - _v.doubElements[i];
        }
        vTemp2.doubElements = temp2;
        return vTemp2;
    }

    public double dotProduct(Vector _v) {

        Vector vTemp = this;
        if(_v.doubElements.length != vTemp.doubElements.length) {
            if(_v.doubElements.length > vTemp.doubElements.length) {
                vTemp = vTemp.reSize(_v.doubElements.length);
            } else
                _v = _v.reSize(vTemp.doubElements.length);
        }
        double[] temp2 = new double[vTemp.doubElements.length];
        double sumOfElements = 0;
        for(int i=0;i<vTemp.doubElements.length;i++){
            temp2[i] = vTemp.doubElements[i] * _v.doubElements[i];
            sumOfElements = sumOfElements + temp2[i];
        }
        return sumOfElements;
    }

    public double cosineSimilarity(Vector _v) {

        Vector vTemp = this;
        if(_v.doubElements.length != vTemp.doubElements.length) {
            if(_v.doubElements.length > vTemp.doubElements.length) {
                vTemp = vTemp.reSize(_v.doubElements.length);
            } else
                _v = _v.reSize(vTemp.doubElements.length);
        }
        double dotProduct = vTemp.dotProduct(_v);
        double tempA = 0;
        double tempB = 0;
        for (int i =0;i<vTemp.doubElements.length;i++){
            tempA = tempA + (vTemp.doubElements[i]*vTemp.doubElements[i]);
            tempB = tempB + (_v.doubElements[i]*_v.doubElements[i]);
        }
        return ((dotProduct)/((Math.sqrt(tempA))*(Math.sqrt(tempB))));
    }

    @Override
    public boolean equals(Object _obj) {
        Vector v = (Vector) _obj;
        boolean boolEquals = true;

        if(this.doubElements.length != v.doubElements.length) {
            boolEquals = false;
            return boolEquals;
        }
        for(int i = 0; i<this.doubElements.length; i++) {
            if(this.doubElements[i] != v.doubElements[i]) {
                boolEquals = false;
                return boolEquals;
            }
        }

        return boolEquals;
    }

    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < this.getVectorSize(); i++) {
            mySB.append(String.format("%.5f", doubElements[i])).append(",");
        }
        mySB.delete(mySB.length() - 1, mySB.length());
        return mySB.toString();
    }
}
