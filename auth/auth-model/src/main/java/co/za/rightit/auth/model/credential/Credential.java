package co.za.rightit.auth.model.credential;

public interface Credential<V> {
	String getType();
	V getValue();
}
