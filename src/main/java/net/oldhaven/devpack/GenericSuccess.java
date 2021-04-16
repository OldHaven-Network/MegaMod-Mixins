package net.oldhaven.devpack;

public class GenericSuccess<T> {
    private T generic;
    private boolean success = false;
    public static <T> GenericSuccess<T> _RSC(T generic, boolean success) {
        GenericSuccess<T> tGenericSuccess = new GenericSuccess<T>();
        tGenericSuccess.generic = generic;
        tGenericSuccess.success = success;
        return tGenericSuccess;
    }

    public boolean _successCheck() {
        this.success = generic != null;
        return this.success;
    }
    public <A> A retreive() {
        return (A) this.generic;
    }

    public <A> GenericSuccess<T> success(ExtRunnable<A> runnable) {
        if(this.success)
            runnable.run((A) this.generic);
        return this;
    }
    public <A> GenericSuccess<T> failure(ExtRunnable<A> runnable) {
        if(!this.success)
            runnable.run((A) this.generic);
        return this;
    }
}

