package terakoyalabo.foundation.kernel

import terakoyalabo.foundation.lifecycle.NodeRetryable
import terakoyalabo.foundation.lifecycle.ResourceReleasable
import terakoyalabo.foundation.lifecycle.ResourceVerifiable

interface DaemonKernel :
    ResourceVerifiable,
    NodeRetryable,
    ResourceReleasable,
    StatusPublishable
