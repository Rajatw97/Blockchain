package com.learning.blockchain.controller;

import com.learning.blockchain.model.Block;
import com.learning.blockchain.model.DefaultResponse;
import com.learning.blockchain.model.GetChainResponse;
import com.learning.blockchain.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("blockchain")
public class BlockchainController {

    @Autowired
    BlockchainService service;

    @Autowired
    DefaultResponse response;

    @Autowired
    GetChainResponse chainResp;

    @Autowired
    Block block;

    @GetMapping(value = "/mine_block", produces = "application/json")
    public DefaultResponse mineBlock(){

            Block prevBlock = service.getPrevBlock();
            int prevProof = prevBlock.getProof();
            int proof = service.proofOfWork(prevProof);
            Block currentBlock = service.createBlock(proof, prevBlock.getCurrentHash());
            block = service.setBlockValues(currentBlock);
            response.setMessage("Congratulations, you just mined a new block!!!");
            response.setBlock(block);
            return response;
        }

    @GetMapping(value = "/get_blockchain", produces = "application/json")
    public GetChainResponse getBlockchain() {
        chainResp.setChain(service.getChain());
        chainResp.setLength(service.getChain().size());
        return chainResp;
    }

    @GetMapping(value = "/is_valid", produces = "application/json")
    public DefaultResponse isChainValid() {
       if(service.isValid(service.getChain()))
           response.setMessage("Blockchain is valid!!!");
       else
           response.setMessage("Blockchain is not valid!!!");
       return response;

    }
}
