package com.aisolutionvoice.api.voiceData.repository;

import com.aisolutionvoice.api.voiceData.entity.VoiceData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceDataRepository extends JpaRepository<VoiceData, Long> {
}