package zy.pointer.j2easy.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zy.pointer.j2easy.business.asset.service.IAssetTransferService;

@Component
public class TxAutoConfirm {

    @Autowired
    IAssetTransferService assetTransferService;

    @Scheduled(fixedRate = 5000)
    public void loop(){
        assetTransferService.getAllUnConfirmTransfer()
                .forEach( assetTransfer -> assetTransferService.handleUnconfirmTransfer( assetTransfer )
        );
    }

}
