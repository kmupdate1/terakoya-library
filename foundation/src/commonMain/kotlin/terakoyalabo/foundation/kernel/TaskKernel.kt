package terakoyalabo.foundation.kernel

import terakoyalabo.foundation.lifecycle.ResourceReleasable
import terakoyalabo.foundation.lifecycle.ResourceVerifiable
import terakoyalabo.foundation.lifecycle.StatePersistable

interface TaskKernel :
    ResourceVerifiable,
    StatePersistable,
    ResourceReleasable
