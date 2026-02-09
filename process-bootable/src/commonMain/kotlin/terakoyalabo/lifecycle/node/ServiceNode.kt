package terakoyalabo.lifecycle.node

import terakoyalabo.lifecycle.core.Enduable
import terakoyalabo.lifecycle.core.ResourceReleasable
import terakoyalabo.lifecycle.core.ResourceVerifiable
import terakoyalabo.lifecycle.core.RetirementNoticable
import terakoyalabo.lifecycle.core.StatusPublishable

interface ServiceNode :
    Enduable,
    ResourceVerifiable,
    RetirementNoticable,
    ResourceReleasable,
    StatusPublishable
