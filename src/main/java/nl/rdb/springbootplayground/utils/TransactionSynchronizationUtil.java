package nl.rdb.springbootplayground.utils;

import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

import org.springframework.transaction.support.TransactionSynchronization;

/**
 * Used to register execution to
 * be executed after the current transaction has been
 * committed, this way we will not run into lazy init issues.
 * <p>
 * The registration is bound to this current thread, so it will
 * not influence others.
 */
public class TransactionSynchronizationUtil {

    public static void executeAfterCommit(Runnable task) {
        registerSynchronization((AfterCommitSync) task::run);
    }

    @FunctionalInterface
    private interface AfterCommitSync extends TransactionSynchronization {

        @Override
        void afterCommit();
    }
}