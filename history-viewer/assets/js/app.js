const { createApp, ref, shallowRef, computed } = Vue;

const translations = {
    "en": {
        "game.history.room.join": "\uD83D\uDDFA\uFE0F {{ entity }} join the room {{ room }}.",
        "game.history.room.quit": "\uD83D\uDDFA\uFE0F {{ entity }} left the room {{ room }}.",

        "game.history.entity.eliminate": "\uD83D\uDC80 {{ entity }} is eliminate. (TOP {{ position }})",
        "game.history.winner": "\uD83C\uDFC6 {{ entity }} won the Room Royale. (TOP 1)",

        "game.history.action.inactivity": "⚔\uFE0F {{ entity }} takes {{ damage }} damage(s) for inactivity.",

        "game.history.action.heal.success": "\uD83C\uDF7E {{ entity }} used potion. +{{ heal }} PV.",
        "game.history.action.heal.failure": "\uD83C\uDF7E {{ entity }} tried to drink a potion even though he didn't have one.",

        "game.history.action.move.try": "\uD83D\uDDFA\uFE0F {{ entity }} is trying to change rooms.",
        "game.history.action.move.failure": "\uD83D\uDDFA\uFE0F {{ entity }} was unable to change rooms.",

        "game.history.action.attack.already.eliminate": "⚔\uFE0F {{ entity }} tried to attack {{ target }} even though he had already been eliminated.",
        "game.history.action.attack.left.room": "⚔\uFE0F {{ entity }} tried to attack {{ target }}, but he was no longer in the room.",

        "game.history.action.attack.take": "⚔\uFE0F {{ entity }} took {{ damage }} damage from {{ target }}'s attack.",
        "game.history.action.attack.take.crit": "⚔\uFE0F {{ entity }} took {{ damage }} damage from {{ target }}'s attack. [CRITIQUE]",

        "game.history.action.attack.dealt": "⚔\uFE0F {{ entity }} dealt {{ damage }} damage to Neutron.",
        "game.history.action.attack.dealt.crit": "⚔\uFE0F {{ entity }} dealt {{ damage }} damage to Neutron. [CRITIQUE]",

        "game.history.action.kill": "\uD83D\uDC80 {{ entity }} killed {{ target }}.",
        "game.history.action.killed.by": "\uD83D\uDC80 {{ entity }} was killed by {{ target }}.",

        "game.history.action.defense.self": "\uD83D\uDEE1\uFE0F {{ entity }} defended himself against {{ target }}'s attack.",
        "game.history.action.defense.failed": "\uD83D\uDEE1\uFE0F {{ entity }}'s attack on {{ target }} failed."
    },
    "fr": {
        "game.history.room.join": "\uD83D\uDDFA\uFE0F {{ entity }} a rejoint la salle {{ room }}.",
        "game.history.room.quit": "\uD83D\uDDFA\uFE0F {{ entity }} a quitté la salle {{ room }}.",

        "game.history.entity.eliminate": "\uD83D\uDC80 {{ entity }} est éliminé. (TOP {{ position }})",
        "game.history.winner": "\uD83C\uDFC6 {{ entity }} a remporté le Room Royale. (TOP 1)",

        "game.history.action.inactivity": "⚔\uFE0F {{ entity }} subit {{ damage }} point(s) de dégâts pour inactivité.",

        "game.history.action.heal.success": "\uD83C\uDF7E {{ entity }} a utilisé une potion. +{{ heal }} PV.",
        "game.history.action.heal.failure": "\uD83C\uDF7E {{ entity }} a essayé de boire une potion alors qu'il n'en avait pas.",

        "game.history.action.move.try": "\uD83D\uDDFA\uFE0F {{ entity }} essaie de changer de salle.",
        "game.history.action.move.failure": "\uD83D\uDDFA\uFE0F {{ entity }} n'a pas pu changer de salle.",

        "game.history.action.attack.already.eliminate": "⚔\uFE0F {{ entity }} a tenté d'attaquer {{ target }} alors qu'il était déjà éliminé.",
        "game.history.action.attack.left.room": "⚔\uFE0F {{ entity }} a tenté d'attaquer {{ target }}, mais celui-ci n'était plus dans la salle.",

        "game.history.action.attack.take": "⚔\uFE0F {{ entity }} a subi {{ damage }} point(s) de dégâts de l'attaque de {{ target }}.",
        "game.history.action.attack.take.crit": "⚔\uFE0F {{ entity }} a subi {{ damage }} point(s) de dégâts de l'attaque de {{ target }}. [CRITIQUE]",

        "game.history.action.attack.dealt": "⚔\uFE0F {{ entity }} a infligé {{ damage }} point(s) de dégâts à {{ target }}.",
        "game.history.action.attack.dealt.crit": "⚔\uFE0F {{ entity }} a infligé {{ damage }} point(s) de dégâts à {{ target }}. [CRITIQUE]",

        "game.history.action.kill": "\uD83D\uDC80 {{ entity }} a tué {{ target }}.",
        "game.history.action.killed.by": "\uD83D\uDC80 {{ entity }} a été tué par {{ target }}.",

        "game.history.action.defense.self": "\uD83D\uDEE1\uFE0F {{ entity }} s'est défendu contre l'attaque de {{ target }}.",
        "game.history.action.defense.failed": "\uD83D\uDEE1\uFE0F L'attaque de {{ entity }} sur {{ target }} a échoué."
    }
}

createApp({
    setup() {
        const savedTheme = localStorage.getItem('theme') || 'dark';
        const isDarkMode = ref(savedTheme === 'dark');
        const step = ref('home');
        const progressWidth = ref(0);
        const gameName = ref('');
        const gameData = shallowRef(null);
        const currentLang = ref('fr');

        const activeTab = ref('leaderboard');
        const selectedPlayerId = ref('');

        document.documentElement.setAttribute('data-theme', savedTheme);

        const toggleTheme = () => {
            isDarkMode.value = !isDarkMode.value;
            const newTheme = isDarkMode.value ? 'dark' : 'light';

            // On applique l'attribut sur la balise <html>
            document.documentElement.setAttribute('data-theme', newTheme);
            // On sauvegarde dans le navigateur du joueur
            localStorage.setItem('theme', newTheme);
        };

        const totalActions = computed(() => {
            return gameData.value?.histories?.length || 0;
        });

        const sortedEntities = computed(() => {
            if (!gameData.value?.entities) return [];
            return [...gameData.value.entities].sort((a, b) => a.position - b.position);
        });

        const filteredHistories = computed(() => {
            if (!gameData.value?.histories) return [];

            if (!selectedPlayerId.value) return gameData.value.histories;

            return gameData.value.histories.filter(log => log.profile?.id === selectedPlayerId.value);
        });

        const translate = (log) => {
            const messageKey = log.entry.message;
            let template = translations[currentLang.value]?.[messageKey] || translations['fr']?.[messageKey];

            if (!template) return messageKey;

            if (log.entry.parameters) {
                log.entry.parameters.forEach(param => {
                    template = template.replaceAll(`{{ ${param.data.key} }}`, param.data.value);
                });
            }

            return template;
        };

        const handleFileUpload = (event) => {
            const file = event.target.files[0];
            if (!file) return;

            gameName.value = file.name.replace(/\.json$/i, '');
            step.value = 'loading';
            progressWidth.value = 0;

            const reader = new FileReader();

            reader.onload = (e) => {
                try {
                    const parsedData = JSON.parse(e.target.result);

                    let progress = 0;
                    const interval = setInterval(() => {
                        progress += 25;
                        progressWidth.value = progress;

                        if (progress >= 100) {
                            clearInterval(interval);
                            setTimeout(() => {
                                gameData.value = parsedData;
                                step.value = 'dashboard';
                            }, 200);
                        }
                    }, 100);

                } catch (error) {
                    alert("Le fichier JSON est corrompu ou invalide.");
                    step.value = 'home';
                }
            };

            reader.readAsText(file);
        };

        const reset = () => {
            gameName.value = '';
            gameData.value = null;
            progressWidth.value = 0;
            step.value = 'home';
            activeTab.value = 'leaderboard';
            selectedPlayerId.value = '';
        };

        return {
            isDarkMode,
            toggleTheme,
            step,
            progressWidth,
            gameName,
            gameData,
            totalActions,
            currentLang,
            activeTab,
            sortedEntities,
            selectedPlayerId,
            filteredHistories,
            translate,
            handleFileUpload,
            reset
        };
    }
}).mount('#app');