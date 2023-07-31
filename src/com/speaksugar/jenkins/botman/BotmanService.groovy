package com.speaksugar.jenkins.botman

import com.speaksugar.jenkins.util.HttpUtil

class BotmanService {
    String botmanBaseUrl
    String botmanToken

    /**
     * Send message to Glip team
     * @param teamId
     * @param message
     */
    void sendMessageToTeam(String teamId, String message) {
        sendMessageToTeam([
                'teamId': teamId,
                'message': message,
        ])
        HttpUtil.post(botmanBaseUrl + '/v2/user/message', [
                'teamId': teamId,
                'message': message,
        ], botmanToken)
    }

    /**
     * Send message to Glip user
     * @param userEmail
     * @param message
     */
    void sendMessageToUser(String userEmail, String message) {
        HttpUtil.post(botmanBaseUrl + '/v2/user/message', [
                'email': userEmail,
                'message': message,
                'emailAutoCorrect': true
        ], botmanToken)
    }
}
