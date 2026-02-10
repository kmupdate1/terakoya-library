package terakoyalabo.foundation.kernel

import terakoyalabo.foundation.lifecycle.Endurable
import terakoyalabo.foundation.lifecycle.KernelLaunchable
import terakoyalabo.foundation.lifecycle.ResourceReleasable
import terakoyalabo.foundation.lifecycle.ResourceVerifiable
import terakoyalabo.foundation.lifecycle.RetirementNoticable

interface ServiceKernel :
    Endurable,
    KernelLaunchable,
    ResourceVerifiable,
    RetirementNoticable,
    ResourceReleasable,
    StatusPublishable
