package se.yrgo.services.calls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.domain.Action;
import se.yrgo.domain.Call;
import se.yrgo.services.customers.CustomerManagementService;
import se.yrgo.services.customers.CustomerNotFoundException;
import se.yrgo.services.diary.DiaryManagementService;
import se.yrgo.services.diary.DiaryManagementServiceMockImpl;

import java.util.Collection;

@Transactional
@Service("callService")
public class CallHandlingServiceImpl implements CallHandlingService {
    @Autowired
    private final CustomerManagementService customerManagementService;

    @Autowired
    private final DiaryManagementService diaryManagementService;

    public CallHandlingServiceImpl(CustomerManagementService customerManagementService, DiaryManagementService diaryManagementService) {
        this.customerManagementService = customerManagementService;
        this.diaryManagementService = diaryManagementService;
    }

    @Override
    public void recordCall(String customerId, Call newCall, Collection<Action> actions) throws CustomerNotFoundException {
        customerManagementService.recordCall(customerId, newCall);

        for (Action action : actions) {
            diaryManagementService.recordAction(action);
        }
    }
}
