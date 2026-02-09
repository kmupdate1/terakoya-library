package terakoyalabo.lifecycle.node

import terakoyalabo.lifecycle.core.NodeRetryable
import terakoyalabo.lifecycle.core.ResourceReleasable
import terakoyalabo.lifecycle.core.ResourceVerifiable
import terakoyalabo.lifecycle.core.StatusPublishable

interface DaemonNode :
    ResourceVerifiable,
    NodeRetryable,
    StatusPublishable,
    ResourceReleasable
