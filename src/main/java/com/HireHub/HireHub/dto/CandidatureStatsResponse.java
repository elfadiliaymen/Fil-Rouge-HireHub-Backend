package com.HireHub.HireHub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CandidatureStatsResponse {

    private long total;
    private long enAttente;
    private long acceptees;
    private long refusees;

    public CandidatureStatsResponse(long total, long enAttente, long acceptees, long refusees) {
        this.total = total;
        this.enAttente = enAttente;
        this.acceptees = acceptees;
        this.refusees = refusees;
    }
}