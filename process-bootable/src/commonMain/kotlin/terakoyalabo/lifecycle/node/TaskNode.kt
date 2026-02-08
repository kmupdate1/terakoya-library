package terakoyalabo.lifecycle.node

interface TaskNode :
    ResourceVerifiable,
    StatePersistant,
    ResourceReleasable
