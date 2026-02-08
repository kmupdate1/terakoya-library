package terakoyalabo.lifecycle.node

interface DaemonNode :
    ResourceVerifiable,
    Retryable,
    StatusPublishable,
    ResourceReleasable
