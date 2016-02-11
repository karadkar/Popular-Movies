package com.example.rnztx.popularmovies.modules;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonHandler {
    private String LOG_TAG = JsonHandler.class.getSimpleName();
    private String rawJsonData = null;

    //required fields from Jason Data
    private final String TMDB_RESULTS = "results";
    private final String ORIGINAL_TITLE = "original_title";
    private final String POSTER_PATH = "poster_path";
    private final String PLOT = "overview";
    private final String VOTE_AVG = "vote_average";
    private final String RELEASE_DATE = "release_date";

    public JsonHandler(){
        this.rawJsonData = "{\"page\":1,\"results\":[{\"poster_path\":\"\\/inVq3FRqcYIRl2la8iZikYYxFNR.jpg\",\"adult\":false,\"overview\":\"Based upon Marvel Comics’ most unconventional anti-hero, DEADPOOL tells the origin story of former Special Forces operative turned mercenary Wade Wilson, who after being subjected to a rogue experiment that leaves him with accelerated healing powers, adopts the alter ego Deadpool. Armed with his new abilities and a dark, twisted sense of humor, Deadpool hunts down the man who nearly destroyed his life.\",\"release_date\":\"2016-02-09\",\"genre_ids\":[35,12,28,878],\"id\":293660,\"original_title\":\"Deadpool\",\"original_language\":\"en\",\"title\":\"Deadpool\",\"backdrop_path\":\"\\/nbIrDhOtUpdD9HKDBRy02a8VhpV.jpg\",\"popularity\":53.024395,\"vote_count\":229,\"video\":false,\"vote_average\":5.77},{\"poster_path\":\"\\/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg\",\"adult\":false,\"overview\":\"Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.\",\"release_date\":\"2015-06-09\",\"genre_ids\":[28,12,878,53],\"id\":135397,\"original_title\":\"Jurassic World\",\"original_language\":\"en\",\"title\":\"Jurassic World\",\"backdrop_path\":\"\\/dkMD5qlogeRMiEixC4YNPUvax2T.jpg\",\"popularity\":32.678475,\"vote_count\":3815,\"video\":false,\"vote_average\":6.73},{\"poster_path\":\"\\/oXUWEc5i3wYyFnL1Ycu8ppxxPvs.jpg\",\"adult\":false,\"overview\":\"In the 1820s, a frontiersman, Hugh Glass, sets out on a path of vengeance against those who left him for dead after a bear mauling.\",\"release_date\":\"2015-12-25\",\"genre_ids\":[37,18,12,53],\"id\":281957,\"original_title\":\"The Revenant\",\"original_language\":\"en\",\"title\":\"The Revenant\",\"backdrop_path\":\"\\/uS1SkjVviraGfFNgkDwe7ohTm8B.jpg\",\"popularity\":27.884875,\"vote_count\":1320,\"video\":false,\"vote_average\":7.28},{\"poster_path\":\"\\/kqjL17yufvn9OVLyXYpvtyrFfak.jpg\",\"adult\":false,\"overview\":\"An apocalyptic story set in the furthest reaches of our planet, in a stark desert landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. Within this world exist two rebels on the run who just might be able to restore order. There's Max, a man of action and a man of few words, who seeks peace of mind following the loss of his wife and child in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to survival may be achieved if she can make it across the desert back to her childhood homeland.\",\"release_date\":\"2015-05-13\",\"genre_ids\":[878,53,28,12],\"id\":76341,\"original_title\":\"Mad Max: Fury Road\",\"original_language\":\"en\",\"title\":\"Mad Max: Fury Road\",\"backdrop_path\":\"\\/tbhdm8UJAb4ViCTsulYFL3lxMCd.jpg\",\"popularity\":27.293029,\"vote_count\":3665,\"video\":false,\"vote_average\":7.48},{\"poster_path\":\"\\/hE24GYddaxB9MVZl1CaiI86M3kp.jpg\",\"adult\":false,\"overview\":\"A cryptic message from Bond’s past sends him on a trail to uncover a sinister organization. While M battles political forces to keep the secret service alive, Bond peels back the layers of deceit to reveal the terrible truth behind SPECTRE.\",\"release_date\":\"2015-10-26\",\"genre_ids\":[28,12,80],\"id\":206647,\"original_title\":\"Spectre\",\"original_language\":\"en\",\"title\":\"Spectre\",\"backdrop_path\":\"\\/wVTYlkKPKrljJfugXN7UlLNjtuJ.jpg\",\"popularity\":22.722723,\"vote_count\":1972,\"video\":false,\"vote_average\":6.3},{\"poster_path\":\"\\/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg\",\"adult\":false,\"overview\":\"During a manned mission to Mars, Astronaut Mark Watney is presumed dead after a fierce storm and left behind by his crew. But Watney has survived and finds himself stranded and alone on the hostile planet. With only meager supplies, he must draw upon his ingenuity, wit and spirit to subsist and find a way to signal to Earth that he is alive.\",\"release_date\":\"2015-09-30\",\"genre_ids\":[18,12,878],\"id\":286217,\"original_title\":\"The Martian\",\"original_language\":\"en\",\"title\":\"The Martian\",\"backdrop_path\":\"\\/sy3e2e4JwdAtd2oZGA2uUilZe8j.jpg\",\"popularity\":22.220299,\"vote_count\":2456,\"video\":false,\"vote_average\":7.6},{\"poster_path\":\"\\/fYzpM9GmpBlIC893fNjoWCwE24H.jpg\",\"adult\":false,\"overview\":\"Thirty years after defeating the Galactic Empire, Han Solo and his allies face a new threat from the evil Kylo Ren and his army of Stormtroopers.\",\"release_date\":\"2015-12-15\",\"genre_ids\":[28,12,878,14],\"id\":140607,\"original_title\":\"Star Wars: The Force Awakens\",\"original_language\":\"en\",\"title\":\"Star Wars: The Force Awakens\",\"backdrop_path\":\"\\/c2Ax8Rox5g6CneChwy1gmu4UbSb.jpg\",\"popularity\":20.696456,\"vote_count\":2973,\"video\":false,\"vote_average\":7.78},{\"poster_path\":\"\\/cgxEscv6TQRK6a514FwuJOjcqQ5.jpg\",\"adult\":false,\"overview\":\"16-year-old Cassie Sullivan tries to survive in a world devastated by the waves of an alien invasion that has already decimated the population and knocked mankind back to the Stone Age.\",\"release_date\":\"2016-01-14\",\"genre_ids\":[12,878,53],\"id\":299687,\"original_title\":\"The 5th Wave\",\"original_language\":\"en\",\"title\":\"The 5th Wave\",\"backdrop_path\":\"\\/9mNklILambgAb1mUm8912yaSjar.jpg\",\"popularity\":18.72211,\"vote_count\":198,\"video\":false,\"vote_average\":5.11},{\"poster_path\":\"\\/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg\",\"adult\":false,\"overview\":\"The year is 2029. John Connor, leader of the resistance continues the war against the machines. At the Los Angeles offensive, John's fears of the unknown future begin to emerge when TECOM spies reveal a new plot by SkyNet that will attack him from both fronts; past and future, and will ultimately change warfare forever.\",\"release_date\":\"2015-06-23\",\"genre_ids\":[878,28,53,12],\"id\":87101,\"original_title\":\"Terminator Genisys\",\"original_language\":\"en\",\"title\":\"Terminator Genisys\",\"backdrop_path\":\"\\/bIlYH4l2AyYvEysmS2AOfjO7Dn8.jpg\",\"popularity\":17.916123,\"vote_count\":1745,\"video\":false,\"vote_average\":6.11},{\"poster_path\":\"\\/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg\",\"adult\":false,\"overview\":\"Interstellar chronicles the adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.\",\"release_date\":\"2014-11-05\",\"genre_ids\":[12,18,878],\"id\":157336,\"original_title\":\"Interstellar\",\"original_language\":\"en\",\"title\":\"Interstellar\",\"backdrop_path\":\"\\/xu9zaAevzQ5nnrsXN6JcahLnG4i.jpg\",\"popularity\":17.77444,\"vote_count\":4365,\"video\":false,\"vote_average\":8.23},{\"poster_path\":\"\\/2ZckiMTfSkCep2JTtZbr73tnQbN.jpg\",\"adult\":false,\"overview\":\"An epic journey into the world of dinosaurs where an Apatosaurus named Arlo makes an unlikely human friend.\",\"release_date\":\"2015-11-14\",\"genre_ids\":[14,16,35,10751,12],\"id\":105864,\"original_title\":\"The Good Dinosaur\",\"original_language\":\"en\",\"title\":\"The Good Dinosaur\",\"backdrop_path\":\"\\/pDuD96Fz0ZZXf9buEvRu1UQsmFT.jpg\",\"popularity\":16.715253,\"vote_count\":396,\"video\":false,\"vote_average\":6.49},{\"poster_path\":\"\\/fqe8JxDNO8B8QfOGTdjh6sPCdSC.jpg\",\"adult\":false,\"overview\":\"Bounty hunters seek shelter from a raging blizzard and get caught up in a plot of betrayal and deception.\",\"release_date\":\"2015-12-25\",\"genre_ids\":[18,9648,53,37],\"id\":273248,\"original_title\":\"The Hateful Eight\",\"original_language\":\"en\",\"title\":\"The Hateful Eight\",\"backdrop_path\":\"\\/sSvgNBeBNzAuKl8U8sP50ETJPgx.jpg\",\"popularity\":15.780125,\"vote_count\":914,\"video\":false,\"vote_average\":7.24},{\"poster_path\":\"\\/cWERd8rgbw7bCMZlwP207HUXxym.jpg\",\"adult\":false,\"overview\":\"Katniss Everdeen reluctantly becomes the symbol of a mass rebellion against the autocratic Capitol.\",\"release_date\":\"2014-11-19\",\"genre_ids\":[878,12,53],\"id\":131631,\"original_title\":\"The Hunger Games: Mockingjay - Part 1\",\"original_language\":\"en\",\"title\":\"The Hunger Games: Mockingjay - Part 1\",\"backdrop_path\":\"\\/83nHcz2KcnEpPXY50Ky2VldewJJ.jpg\",\"popularity\":15.59301,\"vote_count\":2512,\"video\":false,\"vote_average\":6.85},{\"poster_path\":\"\\/q0R4crx2SehcEEQEkYObktdeFy.jpg\",\"adult\":false,\"overview\":\"Minions Stuart, Kevin and Bob are recruited by Scarlet Overkill, a super-villain who, alongside her inventor husband Herb, hatches a plot to take over the world.\",\"release_date\":\"2015-06-17\",\"genre_ids\":[10751,16,12,35],\"id\":211672,\"original_title\":\"Minions\",\"original_language\":\"en\",\"title\":\"Minions\",\"backdrop_path\":\"\\/uX7LXnsC7bZJZjn048UCOwkPXWJ.jpg\",\"popularity\":15.089998,\"vote_count\":2188,\"video\":false,\"vote_average\":6.67},{\"poster_path\":\"\\/nokkbw7qbEj4qISQSWuTqltkaIo.jpg\",\"adult\":false,\"overview\":\"During the Cold War, the Soviet Union captures U.S. pilot Francis Gary Powers after shooting down his U-2 spy plane. Sentenced to 10 years in prison, Powers' only hope is New York lawyer James Donovan, recruited by a CIA operative to negotiate his release. Donovan boards a plane to Berlin, hoping to win the young man's freedom through a prisoner exchange. If all goes well, the Russians would get Rudolf Abel, the convicted spy who Donovan defended in court.\",\"release_date\":\"2015-10-15\",\"genre_ids\":[53,18],\"id\":296098,\"original_title\":\"Bridge of Spies\",\"original_language\":\"en\",\"title\":\"Bridge of Spies\",\"backdrop_path\":\"\\/bTgqCfMeBgmkcZr2Zw7xO8WlhzK.jpg\",\"popularity\":14.521836,\"vote_count\":657,\"video\":false,\"vote_average\":6.88},{\"poster_path\":\"\\/t5tGykRvvlLBULIPsAJEzGg1ylm.jpg\",\"adult\":false,\"overview\":\"A father is without the means to pay for his daughter's medical treatment. As a last resort, he partners with a greedy co-worker to rob a casino. When things go awry they're forced to hijack a city bus.\",\"release_date\":\"2015-11-13\",\"genre_ids\":[28,53],\"id\":336004,\"original_title\":\"Heist\",\"original_language\":\"en\",\"title\":\"Heist\",\"backdrop_path\":\"\\/cBlnfR0n1GA2vPoUQNcbL9pb3VW.jpg\",\"popularity\":14.459718,\"vote_count\":85,\"video\":false,\"vote_average\":5.87},{\"poster_path\":\"\\/noUp0XOqIcmgefRnRZa1nhtRvWO.jpg\",\"adult\":false,\"overview\":\"Based on the real life story of legendary cryptanalyst Alan Turing, the film portrays the nail-biting race against time by Turing and his brilliant team of code-breakers at Britain's top-secret Government Code and Cypher School at Bletchley Park, during the darkest days of World War II.\",\"release_date\":\"2014-11-14\",\"genre_ids\":[36,18,53,10752],\"id\":205596,\"original_title\":\"The Imitation Game\",\"original_language\":\"en\",\"title\":\"The Imitation Game\",\"backdrop_path\":\"\\/qcb6z1HpokTOKdjqDTsnjJk0Xvg.jpg\",\"popularity\":13.693179,\"vote_count\":2505,\"video\":false,\"vote_average\":7.99},{\"poster_path\":\"\\/24qz9HuZY8K3Cdo4hRWkmztT7gH.jpg\",\"adult\":false,\"overview\":\"Continuing his \\\"legendary adventures of awesomeness\\\", Po must face two hugely epic, but different threats: one supernatural and the other a little closer to his home.\",\"release_date\":\"2016-01-23\",\"genre_ids\":[28,12,16,35,10751],\"id\":140300,\"original_title\":\"Kung Fu Panda 3\",\"original_language\":\"en\",\"title\":\"Kung Fu Panda 3\",\"backdrop_path\":\"\\/eHWmEUP4fa7h1Fe7TXfTL7ncDl8.jpg\",\"popularity\":13.460925,\"vote_count\":139,\"video\":false,\"vote_average\":5.73},{\"poster_path\":\"\\/D6e8RJf2qUstnfkTslTXNTUAlT.jpg\",\"adult\":false,\"overview\":\"Armed with the astonishing ability to shrink in scale but increase in strength, con-man Scott Lang must embrace his inner-hero and help his mentor, Dr. Hank Pym, protect the secret behind his spectacular Ant-Man suit from a new generation of towering threats. Against seemingly insurmountable obstacles, Pym and Lang must plan and pull off a heist that will save the world.\",\"release_date\":\"2015-07-14\",\"genre_ids\":[878,28,12],\"id\":102899,\"original_title\":\"Ant-Man\",\"original_language\":\"en\",\"title\":\"Ant-Man\",\"backdrop_path\":\"\\/kvXLZqY0Ngl1XSw7EaMQO0C1CCj.jpg\",\"popularity\":13.220092,\"vote_count\":2553,\"video\":false,\"vote_average\":6.87},{\"poster_path\":\"\\/3WD6E1QcYQHNMPWXEvtivyQ7waQ.jpg\",\"adult\":false,\"overview\":\"When a band of brutal gangsters led by a crooked property developer make a play to take over the city, Master Ip is forced to take a stand.\",\"release_date\":\"2015-12-19\",\"genre_ids\":[28],\"id\":365222,\"original_title\":\"葉問3\",\"original_language\":\"cn\",\"title\":\"Ip Man 3\",\"backdrop_path\":\"\\/1QzLnLBC4dZzMELT89GX79pXlqe.jpg\",\"popularity\":13.155939,\"vote_count\":66,\"video\":false,\"vote_average\":5.2}],\"total_results\":254992,\"total_pages\":12750}";
    }

    public String[] parseData(){
        ArrayList<ArrayList> movieInfoArray = new ArrayList<>();
        try {
            JSONObject TMDB_root = new JSONObject(this.rawJsonData);
            JSONArray resultsArray = TMDB_root.getJSONArray(TMDB_RESULTS);
            for(int i=0; i < resultsArray.length();i++){

                ArrayList<String> information = new ArrayList<>();

                JSONObject singleMovieInfo = resultsArray.getJSONObject(i);
                information.add(singleMovieInfo.getString(ORIGINAL_TITLE));
                information.add(singleMovieInfo.getString(POSTER_PATH));
                information.add(singleMovieInfo.getString(PLOT));
                information.add(singleMovieInfo.getString(VOTE_AVG));
                information.add(singleMovieInfo.getString(RELEASE_DATE));

                movieInfoArray.add(information);
//                Log.e(LOG_TAG,"----------");
//                for (String item : information){
//                    Log.e(LOG_TAG,item);
//                }
            }
            for (ArrayList item : movieInfoArray){
                Log.e(LOG_TAG,"----------");
                for (int i=0; i < item.size(); i++){
                    Log.e(LOG_TAG,item.get(i).toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
