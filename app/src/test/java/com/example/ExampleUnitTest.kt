package com.example

import com.example.data.repository.AsalAbadRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testCreateGroupWithPani() {
        val repo = AsalAbadRepository()
        val initialRoomCount = repo.chatRooms.value.size

        val newRoom = repo.createGroup("گپ دوستانه", "توضیحات", "🌸", withPani = true)
        val rooms = repo.chatRooms.value
        assertEquals(initialRoomCount + 1, rooms.size)

        assertEquals("گپ دوستانه", newRoom.title)

        val messages = repo.messagesMap.value[newRoom.id]
        assertNotNull(messages)
        assertTrue(messages?.any { it.senderName.contains("پانی") } == true)
    }

    @Test
    fun testUpdateBankCard() {
        val repo = AsalAbadRepository()
        repo.updateBankAccount(
            cardNumber = "5022-2910-9988-7766",
            cardHolderName = "کاربر عسل آباد",
            bankName = "بانک پاسارگاد",
            iban = "IR990570000000998877665544"
        )
        val bankInfo = repo.bankAccount.value
        assertEquals("5022-2910-9988-7766", bankInfo.cardNumber)
        assertEquals("کاربر عسل آباد", bankInfo.cardHolderName)
        assertEquals("بانک پاسارگاد", bankInfo.bankName)
    }

    @Test
    fun testPurchaseRecord() {
        val repo = AsalAbadRepository()
        val initialCount = repo.purchaseList.value.size
        val item = repo.recordBankPurchase("بسته طلایی", 5000L, 50000L, "درگاه شاپرک", "RRN-998877")

        val list = repo.purchaseList.value
        assertEquals(initialCount + 1, list.size)
        assertEquals("RRN-998877", item.refId)
    }

    @Test
    fun testNotificationModels() {
        val notif = com.example.data.model.InAppNotification(
            title = "پانی 🌸",
            message = "سلام عشقم!",
            iconEmoji = "🌸",
            type = com.example.data.model.NotificationType.CHAT_MESSAGE,
            targetRoomId = "room_pani"
        )
        assertEquals("پانی 🌸", notif.title)
        assertEquals(com.example.data.model.NotificationType.CHAT_MESSAGE, notif.type)
        assertEquals("room_pani", notif.targetRoomId)
    }

    @Test
    fun testChatAndMessageSearch() {
        val repo = AsalAbadRepository()
        val rooms = repo.chatRooms.value
        val messagesMap = repo.messagesMap.value

        // Test room search by title
        val darkLandRooms = rooms.filter { it.title.contains("DARK", ignoreCase = true) }
        assertTrue(darkLandRooms.isNotEmpty())
        assertEquals("room_dark_land", darkLandRooms.first().id)

        // Test room search by last message
        val pariaRooms = rooms.filter { it.lastMessage.contains("خوش اومدی", ignoreCase = true) }
        assertTrue(pariaRooms.isNotEmpty())

        // Test message search across all rooms
        val query = "الماس"
        val matchedMessages = mutableListOf<com.example.data.model.ChatMessage>()
        rooms.forEach { room ->
            val msgs = messagesMap[room.id] ?: emptyList()
            matchedMessages.addAll(msgs.filter { it.text.contains(query, ignoreCase = true) })
        }
        assertTrue(matchedMessages.isNotEmpty())
        assertTrue(matchedMessages.any { it.text.contains("الماس") })
    }
}
