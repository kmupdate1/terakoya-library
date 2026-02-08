package terakoyalabo.lifecycle.node

interface ServiceNode :
    ResourceVerifiable,
    StatusPublishable,
    RetirementNoticable,
    ResourceReleasable
