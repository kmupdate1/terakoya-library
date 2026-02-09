package terakoyalabo.lifecycle.node

import terakoyalabo.lifecycle.core.ResourceReleasable
import terakoyalabo.lifecycle.core.ResourceVerifiable
import terakoyalabo.lifecycle.core.StatePersistable

interface TaskNode :
    ResourceVerifiable,
    StatePersistable,
    ResourceReleasable
