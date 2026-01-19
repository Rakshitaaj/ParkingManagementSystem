package com.ey;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ey.entity.CustomerIdProof;
import com.ey.entity.User;
import com.ey.enums.IdProofType;

class CustomerIdProofTest {

    @Test
    void customer_id_proof_basic_test() {

        User customer = new User();
        customer.setUserId(2L);

        CustomerIdProof idProof = new CustomerIdProof();
        idProof.setId(1L);
        idProof.setIdProofType(IdProofType.AADHAAR);
        idProof.setIdProofNumber("1234-5678-9012");
        idProof.setCustomer(customer);

        assertNotNull(idProof);
        assertEquals(IdProofType.AADHAAR, idProof.getIdProofType());
        assertEquals("1234-5678-9012", idProof.getIdProofNumber());
        assertNotNull(idProof.getCustomer());
        assertEquals(2L, idProof.getCustomer().getUserId());
    }
}
