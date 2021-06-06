package com.learning.blockchain.service;

import com.learning.blockchain.model.Block;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockchainService {
    ArrayList<Block> chain;

    int proof;

    String prevoiusHash;

    @Autowired
    Block block;

    public BlockchainService(){
        this.chain=new ArrayList<>();
        this.createBlock(1,"0");
    }

    public Block createBlock(int proof, String prevoiusHash){
        Block newBlock=new Block();
        newBlock.setIndex(chain.size()+1);
        newBlock.setProof(proof);
        newBlock.setPreviousHash(prevoiusHash);
        newBlock.setTimestamp(String.valueOf(new Timestamp(System.currentTimeMillis())));
        newBlock.setCurrentHash(hashBlock(newBlock));
        chain.add(newBlock);
        return newBlock;
    }

    public List<Block> getChain(){
        return chain;
    }
    public Block getPrevBlock(){
        return chain.get(chain.size()-1);
    }
    public int proofOfWork(int prevoiusProof) {
        int newproof = 1;
        boolean checkproof = false;
        while (!checkproof) {
            String hashOperation = DigestUtils.sha256Hex(String.valueOf(newproof - prevoiusProof));
            if (hashOperation.substring(0, 4).equals("0000"))
                checkproof = true;
            else
                newproof += 1;

        }
        return newproof;
    }

    public String hashBlock(Block block){
        return DigestUtils.sha256Hex(String.valueOf(block));
    }


    public Block setBlockValues(Block currentBlock){
        block.setIndex(currentBlock.getIndex());
        block.setTimestamp(currentBlock.getTimestamp());
        block.setPreviousHash(currentBlock.getPreviousHash());
        block.setProof(currentBlock.getProof());
        block.setCurrentHash(currentBlock.getCurrentHash());
        return block;
    }

    public  boolean isValid(List<Block> chain){
        int index=chain.size()-1;
        while(index>1){
            if(chain.get(index).getPreviousHash()!=chain.get(index-1).getCurrentHash())
                return false;
            else
                index--;
        }
        return true;

    }



}

