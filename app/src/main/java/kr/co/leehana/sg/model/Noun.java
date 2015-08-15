package kr.co.leehana.sg.model;

/**
 * Created by Hana Lee on 2015-08-12 23:01
 *
 * @author Hana Lee
 * @since 2015-08-12 23:01
 */
public class Noun extends Word {

	public String[] getData() {
		if (data == null || data.length == 0) {
			data = new String[]{"물푸레나무", "바위", "씨앗", "손마디", "물소리", "추위", "샛길", "나뭇잎", "소금쟁이", "절판", "수천권의사전", "거절", "부탁", "개정판", "생활", "목록", "가격", "꼬리말", "머리말", "기승전결", "교육", "가르침", "깨달음", "시간", "공간", "머무름", "둥지", "보금자리", "변기", "고향", "교회", "스님", "목사", "스승", "시인", "시", "문학", "철학", "마음", "노래", "대낮", "밤", "새벽", "오후", "일요일", "토요일", "섬마을", "시련", "고난", "고통", "미소", "철부지", "요구", "유혹", "혼돈", "혼란", "병목현상", "지름길", "터널", "건망증", "욕망", "노예", "시름", "눈물", "울음소리", "노랫소리", "빨래소리", "박음질", "달음질", "탈출", "탈옥", "구속", "고뇌", "번뇌", "번민", "회한", "그림", "음악", "선율", "증명사진", "스틸샷", "카메라", "렌즈", "추억", "과거", "현재", "미래", "현존", "본질", "존재", "후회", "반복", "연애", "사랑", "실연", "미련", "전화", "통화", "돈", "화폐", "시장", "골목", "길거리", "길", "도로", "통로", "비상구", "뼈", "관절", "살", "피", "혈관", "백혈구", "손가락", "달", "해", "태양", "눈", "귀", "코", "입", "입술", "목", "가슴", "고추", "불알", "섹스", "오르가즘", "절정", "전기", "감전", "혼선", "혓바닥", "밑", "음성", "소리", "청각", "시각", "서글픔", "슬픔", "애달픔", "반대파", "파벌", "난교", "번개", "점심", "반찬", "할머니", "특효약", "방향", "마당", "말", "나무", "산", "들", "강", "바다", "파도", "물결", "감정", "영원", "영감", "환자", "흔들림", "소환", "독", "무덤", "사후세계", "건달", "짠맛", "달콤함", "식초", "소금", "설탕", "갈증", "물", "이팝나무", "머리결", "비듬", "어깨", "흙", "관", "부업", "가게", "벨벳", "똥", "배설", "배출", "하수구", "맨홀뚜껑", "창피", "짜증", "울분", "분노", "포효", "불안", "공포", "두려움", "발작", "광기", "정신병", "직업", "유산", "결별", "꽃집", "가장", "표정", "계절", "향기", "첫사랑", "첫경험", "생리", "달표면", "우주", "원형", "빅뱅", "눈동자", "눈꺼풀", "집", "이사", "유목민", "여정", "인상", "당선", "로또", "과속방지턱", "도로의비닐봉지", "로드킬", "브레이크", "자유시간", "자유", "간식", "접근", "시도", "도착", "관음증", "선정", "화분", "복", "행복", "행운", "팜므파탈", "불빛", "컴퓨터", "세탁소", "아내", "남편", "부부", "새집", "둥지", "청소부", "벼락", "발도장", "봄", "여름", "가을", "겨울", "꽃샘추위", "바람", "나이테", "불법무기", "장마", "자동발사", "자동차", "운전", "지하철역", "정거장", "버스", "자전거", "총구", "장미", "피곤", "프러포즈", "프로젝트", "태아", "어머니", "아버지", "선조", "신석기", "호모사피엔스", "느낌", "속삭임", "순환", "별", "별빛", "눈물", "기미", "뽀드락지", "여드름", "수염", "털", "실밥", "돌출", "전생", "후생", "조바심", "들판", "꽃길", "숨", "생각", "잠", "꿈", "꿈길", "묘지", "친구", "동무", "동지", "애인", "병", "피라미", "낚시", "바다", "곡선", "포경선", "고래잡이", "파괴", "파멸", "신생", "재생", "재현", "바구니", "요람", "소란", "동요", "외침", "난동꾼", "잉태", "사막", "오아시스", "구름", "마음", "생각", "어둠", "해고통지서", "천둥", "일기", "옷자락", "존엄", "동면", "술", "가지", "어미", "새끼", "허공", "공허", "수증기", "땀", "땀띠", "가려움", "지고지순함", "순애보", "고백", "외출", "비밀", "신비", "숭고", "샛별", "껍질", "만발", "등짝", "손뼉", "손빨래 ", "독립", "허점", "돌멩이", "선사시대", "태고", "한방울", "글썽임", "20광년", "녹색피", "심장", "정의", "비정규직", "백수", "전문직", "연봉", "명령", "실직", "명퇴", "지급명령", "손짓", "몸짓", "발짓", "말더듬", "장님", "귀머거리", "소", "고양이", "개", "닭", "황금", "음주가무", "음주운전", "무면허", "눈썹", "치장", "감언이설", "아첨", "허세", "허풍", "진실", "베일", "하늘", "밤", "낮", "로그인", "로그아웃", "재부팅", "출입금지", "모니터속", "혼쭐", "홈런", "언저리", "가장자리", "나비", "날개", "변명", "무용지물", "허무", "공허", "암전", "희극", "비극", "시련", "앞뜰", "뒤뜰", "방문", "가슴", "뜨거움", "나들이", "나무밑", "뿌리", "신발", "가출", "황혼이혼", "온도", "체온", "단풍", "공중", "직선", "곡선", "각선미", "백치미", "발자국", "새장", "새", "자유", "잎", "그늘", "바닥", "책갈피", "팽팽함", "단추", "지퍼", "주름", "응결", "결정", "목청", "깃털", "봉합", "겉감", "안감", "내복", "조각", "쫄티", "맨투맨", "몸빼", "배기팬츠", "부라자", "빤스", "뒤태", "엉덩이", "궁둥이", "교태", "젖가슴", "질", "손바닥", "가랑이", "사타구니", "똥꼬", "이름", "부르짖음", "부르틈", "보풀", "포퓰리즘", "선거", "공약", "미궁", "오리궁둥이", "요가", "댄서", "춤", "발레", "계절", "목소리", "낙역", "아이", "소년", "소녀", "처녀", "숙녀", "남자", "상남자", "조폭", "어르신", "양반", "비위", "복숭털", "복숭뼈", "아킬레스건", "관절", "삶", "생", "비루함", "남루함", "비애", "고마움", "축하", "부재중전화", "신호등", "택시", "깜빡이", "나침반", "태풍의눈", "폭풍", "다이어트 ", "고시생", "고시원", "명찰", "돼지비둘기", "마약", "마야달력", "태양력", "태음력", "캄브리아기", "진화", "유전자", "생명", "샘물", "위대함", "소용돌이", "태몽"};
		}
		return data;
	}
}
