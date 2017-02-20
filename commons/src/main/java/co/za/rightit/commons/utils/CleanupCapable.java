package co.za.rightit.commons.utils;

/**
 * Interface for classes that need to run cleanup code without
 * using Runtime ShutdownHooks (which leaks memory on redeploys)
 */
public interface CleanupCapable {
  /** Execute the cleanup code. */
  void cleanup();
}
